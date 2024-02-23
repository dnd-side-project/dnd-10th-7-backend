package com.sendback.domain.feedback.service;

import com.sendback.domain.feedback.dto.request.SaveFeedbackRequestDto;
import com.sendback.domain.feedback.dto.response.*;
import com.sendback.domain.feedback.entity.Feedback;
import com.sendback.domain.feedback.entity.FeedbackSubmit;
import com.sendback.domain.feedback.repository.FeedbackRepository;
import com.sendback.domain.feedback.repository.FeedbackSubmitRepository;
import com.sendback.domain.project.entity.Project;
import com.sendback.domain.project.service.ProjectService;
import com.sendback.domain.user.entity.User;
import com.sendback.domain.user.service.UserService;
import com.sendback.global.ServiceTest;
import com.sendback.domain.user.entity.Level;
import com.sendback.global.config.image.service.ImageService;
import com.sendback.global.exception.type.BadRequestException;
import com.sendback.global.exception.type.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.sendback.domain.feedback.exception.FeedbackExceptionType.*;
import static com.sendback.domain.feedback.fixture.FeedbackFixture.*;
import static com.sendback.domain.project.exception.ProjectExceptionType.NOT_PROJECT_AUTHOR;
import static com.sendback.domain.project.fixture.ProjectFixture.createDummyProject;
import static com.sendback.domain.user.fixture.UserFixture.createDummyUser;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class FeedbackServiceTest extends ServiceTest {

    @InjectMocks
    FeedbackService feedbackService;

    @Mock
    UserService userService;
    @Mock
    ProjectService projectService;
    @Mock
    ImageService imageService;
    @Mock
    FeedbackRepository feedbackRepository;
    @Mock
    FeedbackSubmitRepository feedbackSubmitRepository;

    private User user = createDummyUser();
    private Project project = createDummyProject(user);
    private Feedback feedback = createDummyFeedback(user, project);
    private final FeedbackSubmit feedbackSubmit = createDummyFeedbackSubmit(user, feedback);

    private final SaveFeedbackRequestDto saveFeedbackRequestDto = MOCK_SAVE_FEEDBACK_REQUEST;

    @BeforeEach
    public void setUp() {
        this.user = spy(createDummyUser());
        this.project = spy(createDummyProject(user));
        this.feedback = spy(createDummyFeedback(user, project));
    }

    @Nested
    @DisplayName("특정 프로젝트 피드백 리스트 조회 시")
    class getFeedbacks {

        @Test
        @DisplayName("로그인 안한 유저가 정상적인 요청시 값을 반환한다.")
        public void success_anonymous() throws Exception {
            //given
            given(projectService.getProjectById(anyLong())).willReturn(project);
            given(feedbackRepository.findTop3ByProjectAndIsDeletedIsFalseOrderByIdDesc(any(Project.class))).willReturn(List.of(feedback));
            given(feedback.getId()).willReturn(1L);

            //when
            GetFeedbacksResponse response = feedbackService.getFeedbacks(null, 1L);

            //then
            assertThat(response.feedbacks().size()).isEqualTo(1L);
            FeedbackResponseDto feedbackResponseDto = response.feedbacks().get(0);
            assertThat(feedbackResponseDto.feedbackId()).isEqualTo(1L);
            assertThat(feedbackResponseDto.title()).isEqualTo(feedback.getTitle());
            assertThat(feedbackResponseDto.rewardMessage()).isEqualTo(feedback.getRewardMessage());
            assertThat(feedbackResponseDto.isSubmitted()).isEqualTo(false);

        }

        @Test
        @DisplayName("로그인 한 유저가 정상적인 접근 시 값을 반환한다.")
        public void success() throws Exception {
            //given
            given(projectService.getProjectById(anyLong())).willReturn(project);
            given(feedbackRepository.findTop3ByProjectAndIsDeletedIsFalseOrderByIdDesc(any(Project.class))).willReturn(List.of(feedback));
            given(userService.getUserById(anyLong())).willReturn(user);
            given(project.isAuthor(user)).willReturn(true);
            given(feedbackSubmitRepository.existsByUserAndFeedbackAndIsDeletedIsFalse(any(User.class), any(Feedback.class))).willReturn(true);
            given(feedback.getId()).willReturn(1L);

            //when
            GetFeedbacksResponse response = feedbackService.getFeedbacks(1L, 1L);

            //then
            assertThat(response.feedbacks().size()).isEqualTo(1L);
            FeedbackResponseDto feedbackResponseDto = response.feedbacks().get(0);
            assertThat(feedbackResponseDto.feedbackId()).isEqualTo(1L);
            assertThat(feedbackResponseDto.title()).isEqualTo(feedback.getTitle());
            assertThat(feedbackResponseDto.rewardMessage()).isEqualTo(feedback.getRewardMessage());
            assertThat(feedbackResponseDto.isSubmitted()).isEqualTo(true);

        }

    }

    @Nested
    @DisplayName("피드백 등록 시")
    class saveFeedback {

        @Test
        @DisplayName("프로젝트 작성자와 같지 않으면 예외를 발생한다.")
        public void fail_notAuthor() throws Exception {
            //given
            given(userService.getUserById(anyLong())).willReturn(user);
            given(projectService.getProjectById(anyLong())).willReturn(project);
            doThrow(new BadRequestException(NOT_PROJECT_AUTHOR)).when(projectService).validateProjectAuthor(any(User.class), any(Project.class));

            //when - then
            assertThatThrownBy(() -> feedbackService.saveFeedback(1L, 1L, saveFeedbackRequestDto))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage(NOT_PROJECT_AUTHOR.getMessage());
        }

        @Test
        @DisplayName("정상적인 요청일 시 피드백을 저장한다.")
        public void success() throws Exception {
            //given
            given(userService.getUserById(anyLong())).willReturn(user);
            given(projectService.getProjectById(anyLong())).willReturn(project);
            doNothing().when(projectService).validateProjectAuthor(any(User.class), any(Project.class));
            given(feedbackRepository.save(any(Feedback.class))).willReturn(feedback);
            given(feedback.getId()).willReturn(1L);

            //when
            FeedbackIdResponseDto feedbackIdResponseDto = feedbackService.saveFeedback(1L, 1L, saveFeedbackRequestDto);

            //then
            assertThat(feedbackIdResponseDto.feedbackId()).isEqualTo(1L);
        }
    }

    @Nested
    @DisplayName("피드백 상세 조회 시")
    class getFeedbackDetail {

        @Test
        @DisplayName("존재하지 않는 피드백 ID이면 오류를 발생한다.")
        public void fail_notExistFeedback() throws Exception {
            //given
            given(feedbackRepository.findById(anyLong())).willReturn(Optional.empty());

            //when - then
            assertThatThrownBy(() -> feedbackService.getFeedbackDetail(1L, 1L))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage(NOT_FOUND_FEEDBACK.getMessage());

        }

        @Test
        @DisplayName("정상적인 요청일 시 값을 반환한다.")
        public void success() throws Exception {
            //given
            given(projectService.getProjectById(anyLong())).willReturn(project);
            given(feedbackRepository.findById(anyLong())).willReturn(Optional.of(feedback));
            given(project.getUser()).willReturn(user);
            given(user.getLevel()).willReturn(Level.ONE);
            given(feedback.getCreatedAt()).willReturn(LocalDateTime.now());

            //when
            FeedbackDetailResponseDto feedbackDetailResponseDto = feedbackService.getFeedbackDetail(1L, 1L);

            //then
            assertThat(feedbackDetailResponseDto.feedbackTitle()).isEqualTo(feedback.getTitle());
            assertThat(feedbackDetailResponseDto.nickname()).isEqualTo(feedback.getUser().getNickname());
            assertThat(feedbackDetailResponseDto.projectTitle()).isEqualTo(feedback.getProject().getTitle());
        }
    }

    @Nested
    @DisplayName("피드백을 제출했을 때")
    class submitFeedback {

        @Test
        @DisplayName("작성자가 피드백을 제출하면 예외를 일으킨다.")
        public void fail_isAuthor() throws Exception {
            //given
            given(userService.getUserById(anyLong())).willReturn(user);
            given(feedbackRepository.findById(anyLong())).willReturn(Optional.of(feedback));
            given(feedback.isAuthor(any(User.class))).willReturn(true);

            //when - then
            assertThatThrownBy(() -> feedbackService.submitFeedback(1L, 1L, mockingMultipartFile("screenShot")))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage(REJECT_SUBMIT_FEEDBACK_BY_AUTHOR.getMessage());
        }

        @Test
        @DisplayName("이미 제출했던 유저면 예외를 발생한다.")
        public void fail_alreadySubmit() throws Exception {
            //given
            given(userService.getUserById(anyLong())).willReturn(user);
            given(feedbackRepository.findById(anyLong())).willReturn(Optional.of(feedback));
            given(feedback.isAuthor(any(User.class))).willReturn(false);
            given(feedbackSubmitRepository.existsByUserAndFeedbackAndIsDeletedIsFalse(any(User.class), any(Feedback.class))).willReturn(true);

            //when - then
            assertThatThrownBy(() -> feedbackService.submitFeedback(1L, 1L, mockingMultipartFile("screenShot")))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage(DUPLICATE_FEEDBACK_SUBMIT.getMessage());
        }

        @Test
        @DisplayName("레벨업이 되지 않는 상태면 유저 레벨을 유지하고 값을 반환한다.")
        public void success_keep() throws Exception {
            //given
            given(userService.getUserById(anyLong())).willReturn(user);
            given(feedbackRepository.findById(anyLong())).willReturn(Optional.of(feedback));
            given(feedback.isAuthor(any(User.class))).willReturn(false);
            given(feedbackSubmitRepository.existsByUserAndFeedbackAndIsDeletedIsFalse(any(User.class), any(Feedback.class))).willReturn(false);
            given(imageService.uploadImage(any(), anyString())).willReturn("sendback1.jpg");
            given(feedbackSubmitRepository.save(any(FeedbackSubmit.class))).willReturn(feedbackSubmit);
            given(feedbackSubmitRepository.countByUserAndIsDeletedIsFalse(any(User.class))).willReturn(1L);
            given(user.getLevel()).willReturn(Level.ONE);

            //when
            SubmitFeedbackResponseDto response = feedbackService.submitFeedback(1L, 1L, mockingMultipartFile("feedback"));

            //then
            assertThat(response.level()).isEqualTo(user.getLevel().getName());
            assertThat(response.isLevelUp()).isFalse();
            assertThat(response.remainFeedbackCount()).isEqualTo(4L);
        }

        @Test
        @DisplayName("레벨업이 되지 않는 상태면 유저 레벨을 유지하고 값을 반환한다.")
        public void success_levelUp() throws Exception {
            //given
            given(userService.getUserById(anyLong())).willReturn(user);
            given(feedbackRepository.findById(anyLong())).willReturn(Optional.of(feedback));
            given(feedback.isAuthor(any(User.class))).willReturn(false);
            given(feedbackSubmitRepository.existsByUserAndFeedbackAndIsDeletedIsFalse(any(User.class), any(Feedback.class))).willReturn(false);
            given(imageService.uploadImage(any(), anyString())).willReturn("sendback1.jpg");
            given(feedbackSubmitRepository.save(any(FeedbackSubmit.class))).willReturn(feedbackSubmit);
            given(feedbackSubmitRepository.countByUserAndIsDeletedIsFalse(any(User.class))).willReturn(5L);
            given(user.getLevel()).willReturn(Level.ONE).willReturn(Level.TWO);

            //when
            SubmitFeedbackResponseDto response = feedbackService.submitFeedback(1L, 1L, mockingMultipartFile("feedback"));

            //then
            assertThat(response.level()).isEqualTo(Level.TWO.getName());
            assertThat(response.isLevelUp()).isTrue();
            assertThat(response.remainFeedbackCount()).isEqualTo(5L);
        }
    }

}
