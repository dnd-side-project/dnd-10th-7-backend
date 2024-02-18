package com.sendback.domain.project.service;

import com.sendback.domain.field.entity.Field;
import com.sendback.domain.field.repository.FieldRepository;
import com.sendback.domain.like.repository.LikeRepository;
import com.sendback.domain.project.dto.request.SaveProjectRequestDto;
import com.sendback.domain.project.dto.request.UpdateProjectRequestDto;
import com.sendback.domain.project.dto.response.GetProjectsResponseDto;
import com.sendback.domain.project.dto.response.ProjectDetailResponseDto;
import com.sendback.domain.project.dto.response.ProjectIdResponseDto;
import com.sendback.domain.project.dto.response.RecommendedProjectResponseDto;
import com.sendback.domain.project.dto.response.PullUpProjectResponseDto;
import com.sendback.domain.project.entity.Project;
import com.sendback.domain.project.entity.ProjectImage;
import com.sendback.domain.project.repository.ProjectImageRepository;
import com.sendback.domain.project.repository.ProjectRepository;
import com.sendback.domain.scrap.entity.Scrap;
import com.sendback.domain.scrap.repository.ScrapRepository;
import com.sendback.domain.user.entity.User;
import com.sendback.domain.user.service.UserService;
import com.sendback.global.ServiceTest;
import com.sendback.global.common.constants.FieldName;
import com.sendback.global.common.CustomPage;
import com.sendback.global.config.image.service.ImageService;
import com.sendback.global.exception.type.BadRequestException;
import com.sendback.global.exception.type.NotFoundException;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.util.*;
import static com.sendback.domain.project.exception.ProjectExceptionType.*;
import static com.sendback.domain.project.fixture.ProjectFixture.*;
import static com.sendback.domain.scrap.fixture.ScrapFixture.createDummyScrap;
import static com.sendback.domain.user.fixture.UserFixture.createDummyUser;
import static com.sendback.global.common.constants.FieldName.GAME;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


public class ProjectServiceTest extends ServiceTest {

    @InjectMocks
    ProjectService projectService;

    @Mock
    UserService userService;
    @Mock
    ImageService imageService;
    @Mock
    ProjectImageRepository projectImageRepository;
    @Mock
    ProjectRepository projectRepository;
    @Mock
    LikeRepository likeRepository;
    @Mock
    ScrapRepository scrapRepository;

    @Mock
    FieldRepository fieldRepository;

    private User user;
    private Project project;
    private final List<MultipartFile> images = List.of(mockingMultipartFile("sendback1.jpg"),
            mockingMultipartFile("sendback2.jpg"), mockingMultipartFile("sendback3.jpg"));
    private final static Long SPYING_PROJECT_ID = 1L;
    private final SaveProjectRequestDto saveProjectRequestDto = MOCK_SAVE_PROJECT_REQUEST_DTO;
    private final UpdateProjectRequestDto updateProjectRequestDto = MOCK_UPDATE_PROJECT_REQUEST_DTO;

    @BeforeEach
    public void setUp() {
        this.user = spy(createDummyUser());
        this.project = spy(createDummyProject(user));
    }

    @Nested
    @DisplayName("프로젝트 상세 조회 시")
    class getProjectDetail {

        @Test
        @DisplayName("로그인 안한 유저가 정상적인 요청 시 값을 반환한다.")
        public void success_anonymous() throws Exception {
            //given
            given(projectRepository.findById(anyLong())).willReturn(Optional.of(project));
            given(user.getId()).willReturn(1L);
            given(project.getId()).willReturn(1L);
            given(project.getCreatedAt()).willReturn(LocalDateTime.now());
            given(project.getLikes()).willReturn(List.of());
            given(project.getScraps()).willReturn(List.of());
            given(project.getComments()).willReturn(List.of());

            //when
            ProjectDetailResponseDto response = projectService.getProjectDetail(null, 1L);


           //then
           assertThat(response.nickname()).isEqualTo(user.getNickname());
           assertThat(response.content()).isEqualTo(project.getContent());
           assertThat(response.likeCount()).isEqualTo(project.getLikes().size());
           assertThat(response.scrapCount()).isEqualTo(project.getScraps().size());
           assertThat(response.commentCount()).isEqualTo(project.getLikes().size());
           assertThat(response.isAuthor()).isFalse();
        }

        @Test
        @DisplayName("로그인 한 유저가 정상적인 요청 시 값을 반환한다.")
        public void success() throws Exception {
            //given
            given(projectRepository.findById(anyLong())).willReturn(Optional.of(project));
            given(userService.getUserById(anyLong())).willReturn(user);
            given(likeRepository.existsByUserAndProjectAndIsDeletedIsFalse(any(User.class), any(Project.class)))
                    .willReturn(true);
            given(scrapRepository.existsByUserAndProjectAndIsDeletedIsFalse(any(User.class), any(Project.class)))
                    .willReturn(false);

            given(user.getId()).willReturn(1L);
            given(project.getId()).willReturn(1L);
            given(project.getCreatedAt()).willReturn(LocalDateTime.now());
            given(project.getLikes()).willReturn(List.of());
            given(project.getScraps()).willReturn(List.of());
            given(project.getComments()).willReturn(List.of());

            //when
            ProjectDetailResponseDto response = projectService.getProjectDetail(1L, 1L);


            //then
            assertThat(response.nickname()).isEqualTo(user.getNickname());
            assertThat(response.content()).isEqualTo(project.getContent());
            assertThat(response.likeCount()).isEqualTo(project.getLikes().size());
            assertThat(response.scrapCount()).isEqualTo(project.getScraps().size());
            assertThat(response.commentCount()).isEqualTo(project.getLikes().size());
            assertThat(response.isAuthor()).isTrue();
            assertThat(response.isCheckedLike()).isTrue();

        }
    }

    @Test
    @DisplayName("정상적인 요청 시 프로젝트를 생성한다. (이미지가 존재하지 않을 때)")
    public void saveProject_success() throws Exception {
        //given
        given(userService.getUserById(any())).willReturn(user);
        given(projectRepository.save(any())).willReturn(project);
        when(project.getId()).thenReturn(SPYING_PROJECT_ID);

        //when
        ProjectIdResponseDto response = projectService.saveProject(1L, saveProjectRequestDto, List.of());

        //then
        assertThat(response.projectId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("정상적인 요청 시 프로젝트를 생성한다. (이미지가 존재할 때)")
    public void saveProjectContainImage_success() throws Exception {
        //given
        given(userService.getUserById(any())).willReturn(user);
        given(imageService.upload(images, "project")).willReturn(List.of("sendback1.jpg", "sendback2.jpg", "sendback3.jpg"));
        given(projectRepository.save(any())).willReturn(project);
        when(project.getId()).thenReturn(SPYING_PROJECT_ID);


        //when
        ProjectIdResponseDto response = projectService.saveProject(1L, saveProjectRequestDto, images);
        //then
        assertThat(response.projectId()).isEqualTo(1L);
        verify(projectImageRepository, times(3)).save(any());
    }

    @Nested
    @DisplayName("프로젝트 수정 시")
    class UpdateProject {
        @Test
        @DisplayName("프로젝트를 찾을 수 없으면 예외를 발생한다.")
        public void fail_notFoundProject() throws Exception {
            //given
            UpdateProjectRequestDto updateProjectRequestDto = new UpdateProjectRequestDto(project.getTitle(), project.getFieldName().getName(), project.getContent(), project.getSummary(), project.getDemoSiteUrl(),
                    project.getStartedAt(), project.getEndedAt(),
                    project.getProgress().toString(), project.getProjectParticipantCount().getPlannerCount(), project.getProjectParticipantCount().getFrontendCount(),
                    project.getProjectParticipantCount().getBackendCount(), project.getProjectParticipantCount().getDesignCount(), List.of());
            given(userService.getUserById(anyLong())).willReturn(user);
            given(projectRepository.findById(anyLong())).willReturn(Optional.empty());

            //when - then
            assertThatThrownBy(() -> projectService.updateProject(1L, 1L, updateProjectRequestDto, List.of()))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage(NOT_FOUND_PROJECT.getMessage());
        }

        @Test
        @DisplayName("글 작성자와 수정자가 다르면 오류를 발생한다.")
        public void fail_notAuthor() throws Exception {
            //given
            given(userService.getUserById(anyLong())).willReturn(user);
            given(projectRepository.findById(anyLong())).willReturn(Optional.of(project));
            given(project.isAuthor(user)).willReturn(false);

            //when - then
            assertThatThrownBy(() -> projectService.updateProject(1L, 1L, updateProjectRequestDto, List.of()))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage(NOT_PROJECT_AUTHOR.getMessage());
        }

        @Test
        @DisplayName("삭제 요청온 url이 올바르지 않으면 오류를 발생한다.")
        public void fail_notFoundDeleteUrls() throws Exception {
            //given
            given(userService.getUserById(anyLong())).willReturn(user);
            given(projectRepository.findById(anyLong())).willReturn(Optional.of(project));
            given(project.isAuthor(user)).willReturn(true);
            given(projectImageRepository.findByProjectAndImageUrl(any(Project.class), anyString())).willReturn(Optional.empty());

            //when - then
            assertThatThrownBy(() -> projectService.updateProject(1L, 1L, updateProjectRequestDto, List.of()))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage(NOT_FOUND_DELETE_IMAGE_URL.getMessage());
        }

        @Test
        @DisplayName("정상적인 요청 시 프로젝트를 수정한다.")
        public void success() throws Exception {
            //given
            ProjectImage projectImage = ProjectImage.of(project, "url");
            given(userService.getUserById(anyLong())).willReturn(user);
            given(projectRepository.findById(anyLong())).willReturn(Optional.of(project));
            given(project.isAuthor(user)).willReturn(true);
            given(projectImageRepository.findByProjectAndImageUrl(any(Project.class), anyString())).willReturn(Optional.of(projectImage));
            doNothing().when(projectImageRepository).delete(any(ProjectImage.class));
            when(project.getId()).thenReturn(SPYING_PROJECT_ID);

            //when
            ProjectIdResponseDto response = projectService.updateProject(1L, 1L, updateProjectRequestDto, images);

            //then
            assertThat(response.projectId()).isEqualTo(1L);
            assertThat(project.getTitle()).isEqualTo(updateProjectRequestDto.title());
        }
    }

    @Nested
    @DisplayName("프로젝트 삭제 시")
    class DeleteProject {

        @Test
        @DisplayName("프로젝트가 존재하지 않으면 예외를 발생한다.")
        public void fail_notFoundProject() throws Exception {
            //given
            given(userService.getUserById(anyLong())).willReturn(user);
            given(projectRepository.findById(anyLong())).willReturn(Optional.empty());

            //when - then
            assertThatThrownBy(() -> projectService.deleteProject(1L, 1L))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage(NOT_FOUND_PROJECT.getMessage());
        }

        @Test
        @DisplayName("프로젝트 작성자와 삭제 요청자가 다르면 오류를 발생한다.")
        public void fail_notAuthor() throws Exception {
            //given
            given(userService.getUserById(anyLong())).willReturn(user);
            given(projectRepository.findById(anyLong())).willReturn(Optional.of(project));
            given(project.isAuthor(user)).willReturn(false);

            //when - then
            assertThatThrownBy(() -> projectService.deleteProject(1L, 1L))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage(NOT_PROJECT_AUTHOR.getMessage());
        }

        @Test
        @DisplayName("정상적인 요청시 프로젝트를 삭제한다.")
        public void success() throws Exception {
            //given
            given(userService.getUserById(anyLong())).willReturn(user);
            given(projectRepository.findById(anyLong())).willReturn(Optional.of(project));
            given(project.isAuthor(user)).willReturn(true);
            given(projectImageRepository.findAllByProject(project)).willReturn(List.of());
            doNothing().when(projectImageRepository).deleteAll(anyList());
            doNothing().when(projectRepository).delete(project);

            //when - then
            assertDoesNotThrow(() -> projectService.deleteProject(1L, 1L));
        }
    }

    @Nested
    @DisplayName("추천 프로젝트 조회")
    class getRecommendedProject {

        @Test
        @DisplayName("성공하면 200과 함께 추천 프로젝트를 반환한다.")
        void getUserInfo_success() {
            // given
            Long mock_userId = 1L;
            List<FieldName> fieldNameList = List.of(GAME); // 수정된 부분
            List<Field> fieldList = new ArrayList<>();
            fieldList.add(Field.of(GAME, user));

            List<RecommendedProjectResponseDto> projects = new ArrayList<>();
            projects.add(MOCK_RECOMMEND_PROJECT_RESPONSE_DTO);

            given(fieldRepository.findAllByUserId(mock_userId)).willReturn(fieldList);
            given(projectRepository.findRecommendedProjects(mock_userId, fieldNameList)).willReturn(projects);

            // when
            List<RecommendedProjectResponseDto> result = projectService.getRecommendedProject(mock_userId);

            // then
            assertThat(projects.get(0).projectId()).isEqualTo(result.get(0).projectId());
            assertThat(projects.get(0).field()).isEqualTo(result.get(0).field());
            assertThat(projects.get(0).createdAt()).isEqualTo(result.get(0).createdAt());
            assertThat(projects.get(0).title()).isEqualTo(result.get(0).title());
            assertThat(projects.get(0).profileImageUrl()).isEqualTo(result.get(0).profileImageUrl());
            assertThat(projects.get(0).createdBy()).isEqualTo(result.get(0).createdBy());
            assertThat(projects.get(0).summary()).isEqualTo(result.get(0).summary());
            assertThat(projects.get(0).progress()).isEqualTo(result.get(0).progress());
        }

    }

    @DisplayName("프로젝트 끌올 시")
    class pullUpProject {

        @Test
        @DisplayName("프로젝트가 존재하지 않으면 예외를 발생한다.")
        public void fail_notFoundProject() throws Exception {
            //given
            given(userService.getUserById(anyLong())).willReturn(user);
            given(projectRepository.findById(anyLong())).willReturn(Optional.empty());

            //when - then
            assertThatThrownBy(() -> projectService.pullUpProject(1L, 1L))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage(NOT_FOUND_PROJECT.getMessage());
        }

        @Test
        @DisplayName("프로젝트 작성자와 끌올 요청자가 다르면 오류를 발생한다.")
        public void fail_notAuthor() throws Exception {
            //given
            given(userService.getUserById(anyLong())).willReturn(user);
            given(projectRepository.findById(anyLong())).willReturn(Optional.of(project));
            given(project.isAuthor(user)).willReturn(false);

            //when - then
            assertThatThrownBy(() -> projectService.pullUpProject(1L, 1L))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage(NOT_PROJECT_AUTHOR.getMessage());
        }

        @Test
        @DisplayName("끌올한지 3일이 안지났으면 오류를 일으킨다.")
        public void fail_needToTime() throws Exception {
            //given
            given(userService.getUserById(anyLong())).willReturn(user);
            given(projectRepository.findById(anyLong())).willReturn(Optional.of(project));
            given(project.isAuthor(user)).willReturn(true);
            given(project.isAvailablePulledUp()).willReturn(false);

            //when - then
            assertThatThrownBy(() -> projectService.pullUpProject(1L, 1L))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage(NEED_TO_TIME_FOR_PULL_UP.getMessage());
        }

        @Test
        @DisplayName("프로젝트 끌올 횟수를 넘기면 오류를 일으킨다.")
        public void fail_overProjectPullUpCnt() throws Exception {
            //given
            given(userService.getUserById(anyLong())).willReturn(user);
            given(projectRepository.findById(anyLong())).willReturn(Optional.of(project));
            given(project.isAuthor(user)).willReturn(true);
            given(project.isAvailablePulledUp()).willReturn(true);
            given(project.isOverPullUpCnt()).willReturn(true);
            given(user.isOverPullUpCnt()).willReturn(false);

            //when - then
            assertThatThrownBy(() -> projectService.pullUpProject(1L, 1L))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage(OVER_PROJECT_PULL_UP_CNT.getMessage());
        }

        @Test
        @DisplayName("유저 끌올 횟수를 넘기면 오류를 일으킨다.")
        public void fail_overUserPullUpCnt() throws Exception {
            //given
            given(userService.getUserById(anyLong())).willReturn(user);
            given(projectRepository.findById(anyLong())).willReturn(Optional.of(project));
            given(project.isAuthor(user)).willReturn(true);
            given(project.isAvailablePulledUp()).willReturn(true);
            given(project.isOverPullUpCnt()).willReturn(false);
            given(user.isOverPullUpCnt()).willReturn(true);

            //when - then
            assertThatThrownBy(() -> projectService.pullUpProject(1L, 1L))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage(OVER_USER_PULL_UP_CNT.getMessage());
        }

        @Test
        @DisplayName("정상적인 요청이라면 성공을 반환한다.")
        public void success() throws Exception {
            //given
            given(userService.getUserById(anyLong())).willReturn(user);
            given(projectRepository.findById(anyLong())).willReturn(Optional.of(project));
            given(project.isAuthor(user)).willReturn(true);
            given(project.isAvailablePulledUp()).willReturn(true);
            given(project.isOverPullUpCnt()).willReturn(false);
            given(user.isOverPullUpCnt()).willReturn(false);
            doNothing().when(user).actPullUp();
            doNothing().when(project).pullUp();

            //when
            PullUpProjectResponseDto response = projectService.pullUpProject(1L, 1L);

            //then
            assertThat(response.isPulledUp()).isTrue();

        }
    }

    @Nested
    @DisplayName("프로젝트 전체 조회 시")
    class getProjects {

        @Test
        @DisplayName("로그인 안한 유저가 접근 시 값을 반환한다.")
        public void success_anonymous() throws Exception {
            //given
            Pageable pageable = PageRequest.of(1, 5);
            Pageable changePageable = PageRequest.of(0, 5);

            Page<Project> projectPage = new PageImpl<>(List.of(project), changePageable, 1);

            given(projectRepository.findAllByPageableAndFieldAndIsFinishedAndSort(any(Pageable.class), any(), any(), any(), any()))
                    .willReturn(projectPage);
            //when
            CustomPage<GetProjectsResponseDto> response = projectService.getProjects(null, pageable, null, null, null, null);

            //then
            assertThat(response.getTotalPages()).isEqualTo(1);
            assertThat(response.getContent().get(0).title()).isEqualTo(project.getTitle());
            assertThat(response.getTotalElements()).isEqualTo(1);
        }

        @Test
        @DisplayName("로그인 한 유저가 접근 시 값을 반환한다.")
        public void success() throws Exception {
            //given
            Pageable pageable = PageRequest.of(1, 5);
            Pageable changePageable = PageRequest.of(0, 5);
            Page<Project> projectPage = new PageImpl<>(List.of(project), changePageable, 1);
            Scrap scrap = createDummyScrap(user, project);

            given(projectRepository.findAllByPageableAndFieldAndIsFinishedAndSort(any(Pageable.class), any(), any(), any(), any()))
                    .willReturn(projectPage);
            given(userService.getUserById(anyLong())).willReturn(user);
            given(scrapRepository.findAllByUserAndIsDeletedIsFalse(any(User.class))).willReturn(List.of(scrap));
            given(project.getId()).willReturn(1L);

            //when
            CustomPage<GetProjectsResponseDto> response = projectService.getProjects(1L, pageable, null, null, null, null);

            //then
            assertThat(response.getTotalPages()).isEqualTo(1);
            assertThat(response.getContent().get(0).title()).isEqualTo(project.getTitle());
            assertThat(response.getContent().get(0).isScrapped()).isEqualTo(true);
            assertThat(response.getTotalElements()).isEqualTo(1);
        }
    }
}
