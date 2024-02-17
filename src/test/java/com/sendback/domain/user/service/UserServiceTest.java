package com.sendback.domain.user.service;

import com.sendback.domain.auth.dto.Token;
import com.sendback.domain.feedback.repository.FeedbackSubmitRepository;
import com.sendback.domain.field.entity.Field;
import com.sendback.domain.field.repository.FieldRepository;
import com.sendback.domain.field.service.FieldService;
import com.sendback.domain.like.repository.LikeRepository;
import com.sendback.domain.project.entity.Project;
import com.sendback.domain.project.repository.ProjectRepository;
import com.sendback.domain.user.dto.request.UpdateUserInfoRequestDto;
import com.sendback.domain.user.dto.response.*;
import com.sendback.domain.user.entity.Level;
import com.sendback.domain.user.entity.User;
import com.sendback.domain.user.repository.UserRepository;
import com.sendback.global.ServiceTest;
import com.sendback.global.common.CustomPage;
import com.sendback.global.common.constants.FieldName;
import com.sendback.global.config.jwt.JwtProvider;
import com.sendback.global.exception.type.BadRequestException;
import com.sendback.global.exception.type.SignInException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;
import java.util.stream.Collectors;

import static com.sendback.domain.field.fixture.FieldFixture.createMockFields;
import static com.sendback.domain.project.fixture.ProjectFixture.createDummyProject;
import static com.sendback.domain.user.exception.UserExceptionType.INVALID_NICKNAME;
import static com.sendback.domain.user.exception.UserExceptionType.INVALID_SIGN_TOKEN;
import static com.sendback.domain.user.fixture.UserFixture.*;
import static com.sendback.domain.user.fixture.UserFixture.createDummyUser_C;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class UserServiceTest extends ServiceTest {
    @InjectMocks
    UserService userService;
    @Mock
    JwtProvider jwtProvider;
    @Mock
    UserRepository userRepository;

    @Mock
    ProjectRepository projectRepository;
    @Mock
    FeedbackSubmitRepository feedbackSubmitRepository;

    @Mock
    LikeRepository likeRepository;
    @Mock
    FieldRepository fieldRepository;

    @Mock
    FieldService fieldService;

    private User user = createDummyUser_C();
    private Project project;
    List<Field> mock_Fields = createMockFields(user);

    @BeforeEach
    public void setUp() {
        this.user = spy(createDummyUser_C());
        this.project = spy(createDummyProject(user));
    }

    @Test
    @DisplayName("사용자 추가정보를 통해 회원가입을 진행한다. 성공하면 200과 함께 access token, refresh token을 반환한다.")
    public void signUpUser_success() throws Exception {
        // given
        String signToken = "valid signToken";
        doNothing().when(jwtProvider).validateSignToken(any());
        given(jwtProvider.getSignUserInfo(signToken)).willReturn(MOCK_SIGNING_ACCOUNT);
        given(userRepository.save(any(User.class))).willReturn(mock_user);
        given(jwtProvider.issueToken(mock_user.getId()))
                .willReturn(new Token("valid accessToken", "valid refreshToken"));

        // when
        Token resultToken = userService.signUpUser(MOCK_SIGN_UP_REQUEST_DTO);

        // then
        verify(jwtProvider).validateSignToken(any());
        verify(jwtProvider).getSignUserInfo(signToken);
        verify(userRepository).save(any(User.class));
        verify(jwtProvider).issueToken(mock_user.getId());
        assertNotNull(resultToken);
        assertEquals("valid accessToken", resultToken.accessToken());
        assertEquals("valid refreshToken", resultToken.refreshToken());
    }

    @Test
    @DisplayName("올바르지 않은 sign token으로 회원가입 시 예외 발생")
    public void signUpUser_invalidSignToken_throwsException() {
        // given
        String signToken = "Invalid signToken";
        doThrow(new SignInException(INVALID_SIGN_TOKEN))
                .when(jwtProvider).validateSignToken(signToken);

        // when, then
        assertThatThrownBy(() -> userService.signUpUser(MOCK_INVALID_SIGN_TOKEN_SIGN_UP_REQUEST_DTO))
                .isInstanceOf(SignInException.class)
                .hasMessage(INVALID_SIGN_TOKEN.getMessage());
    }

    @Nested
    @DisplayName("닉네임 중복 검사")
    class checkNickname {

        @Test
        @DisplayName("닉네임 중복이면 check 값은 true이다.")
        void checkNickname_true() {
            // given
            String nickname = "nickname";
            User finduser = User.builder().nickname(nickname).build();
            given(userRepository.findByNickname(nickname)).willReturn(Optional.of(finduser));

            // when
            CheckUserNicknameResponseDto responseDto = userService.checkUserNickname(nickname);

            // then
            assertTrue(responseDto.check());
        }

        @Test
        @DisplayName("닉네임 중복이 아니면 check 값은 false이다.")
        void checkNickname_false() {
            // given
            String nickname = "nickname";
            given(userRepository.findByNickname(nickname)).willReturn(Optional.empty());

            // when
            CheckUserNicknameResponseDto responseDto = userService.checkUserNickname(nickname);

            // then
            assertTrue(!responseDto.check());
        }

        @Test
        @DisplayName("닉네임이 중복이면 2050 상태코드를 반환한다.")
        void checkNickname_BadRequestException() throws Exception {
            // given
            String nickname = "invalid nickname@@@@";

            // when, then
            assertThatThrownBy(() -> userService.checkUserNickname(nickname))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage(INVALID_NICKNAME.getMessage());

        }
    }

    @Nested
    @DisplayName("내 정보 조회")
    class getUserInfo {

        @Test
        @DisplayName("성공하면 200과 함께 유저 정보를 반환한다.")
        void getUserInfo_success() {
            // given
            Long mockUserId = 1L;
            Long projectCount = 2L;
            Long feedbackCount = 2L;
            Long likeCount = 3L;
            Long needToFeedbackCount = Level.getRemainCountUntilNextLevel(feedbackCount);

            given(userRepository.findById(mockUserId)).willReturn(Optional.of(user));
            given(projectRepository.countByUserId(mockUserId)).willReturn(projectCount);
            given(feedbackSubmitRepository.countByUserId(mockUserId)).willReturn(feedbackCount);
            List<Project> projectList = new ArrayList<>();
            projectList.add(project);
            given(projectRepository.findByUserId(mockUserId)).willReturn(projectList);
            given(likeRepository.countByProjectIn(projectList)).willReturn(likeCount);
            given(fieldRepository.findAllByUserId(mockUserId)).willReturn(mock_Fields);
            List<String> mock_fieldNameList = mock_Fields.stream()
                    .map(Field::getName)
                    .map(FieldName::getName)
                    .collect(Collectors.toList());

            // when
            UserInfoResponseDto responseDto = userService.getUserInfo(mockUserId);

            // then
            assertThat(responseDto.nickname()).isEqualTo(user.getNickname());
            assertThat(responseDto.career()).isEqualTo(user.getCareer().getValue());
            assertThat(responseDto.profileImageUrl()).isEqualTo(user.getProfileImageUrl());
            assertThat(responseDto.birthday()).isEqualTo(user.getBirthDay());
            assertThat(responseDto.email()).isEqualTo(user.getEmail());
            assertThat(responseDto.field()).isEqualTo(mock_fieldNameList);
            assertThat(responseDto.level()).isEqualTo(Level.toNumber(user.getLevel()));
            assertThat(responseDto.feedbackCount()).isEqualTo(feedbackCount);
            assertThat(responseDto.needToFeedbackCount()).isEqualTo(needToFeedbackCount);
            assertThat(responseDto.projectCount()).isEqualTo(projectCount);
            assertThat(responseDto.likeCount()).isEqualTo(likeCount);
        }

    }

    @Nested
    @DisplayName("내 정보 수정")
    class updateUserInfo {

        @Test
        @DisplayName("성공하면 200과 함께 수정된 유저 정보를 반환한다.")
        void getUserInfo_success() {
            // given
            Long mockUserId = 1L;
            User user = createDummyUser_C();
            UpdateUserInfoRequestDto requestDto = new UpdateUserInfoRequestDto("테스트",
                    "2000.01.01", "백엔드", Arrays.asList("환경", "게임"));

            given(userRepository.findById(mockUserId)).willReturn(Optional.of(user));
            given(fieldRepository.deleteByUserId(mockUserId)).willReturn(1L);
            given(fieldRepository.saveAll(anyCollection())).willReturn(Collections.emptyList());

            // when
            UpdateUserInfoResponseDto responseDto = userService.updateUserInfo(mockUserId, requestDto);

            // then
            assertThat(responseDto.nickname()).isEqualTo(requestDto.nickname());
            assertThat(responseDto.career()).isEqualTo(requestDto.career());
            assertThat(responseDto.birthday()).isEqualTo(requestDto.birthday());
            assertThat(responseDto.field()).isEqualTo(requestDto.field());
        }

    }

    @Nested
    @DisplayName("내가 등록한 프로젝트 조회")
    class getRegisteredProjects {

        @Test
        @DisplayName("성공하면 200과 함께 프로젝트 리스트 정보들이 반환된다.")
        void getRegisteredProjects_success() {
            // given
            Long userId = 1L;
            int page = 1;
            int size = 5;
            int sort = 0;
            Page<RegisteredProjectResponseDto> mockResponseDtoPage = mock(Page.class);
            Pageable pageable = PageRequest.of(page - 1, size);
            boolean isFinished = sort == 0;
            given(projectRepository.findAllRegisteredProjectsByMe(pageable, userId, isFinished)).willReturn(mockResponseDtoPage);

            // when
            CustomPage<RegisteredProjectResponseDto> responseDtoPage = userService.getRegisteredProjects(userId, page, size, sort);

            // then
            assert responseDtoPage != null;
        }
    }

    @Nested
    @DisplayName("내가 스크랩한 프로젝트 조회")
    class getScrappedProjects {

        @Test
        @DisplayName("성공하면 200과 함께 프로젝트 리스트 정보들이 반환된다.")
        void getScrappedProjects_success() {
            // given
            Long userId = 1L;
            int page = 1;
            int size = 5;
            int sort = 0;
            Page<ScrappedProjectResponseDto> mockResponseDtoPage = mock(Page.class);
            Pageable pageable = PageRequest.of(page - 1, size);
            boolean isFinished = sort == 0;
            given(projectRepository.findAllScrappedProjectsByMe(pageable, userId, isFinished)).willReturn(mockResponseDtoPage);

            // when
            CustomPage<ScrappedProjectResponseDto> responseDtoPage = userService.getScrappedProjects(userId, page, size, sort);

            // then
            assert responseDtoPage != null;
        }
    }
}
