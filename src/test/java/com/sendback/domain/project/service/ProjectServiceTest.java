package com.sendback.domain.project.service;

import com.sendback.domain.like.repository.LikeRepository;
import com.sendback.domain.project.dto.request.SaveProjectRequestDto;
import com.sendback.domain.project.dto.request.UpdateProjectRequestDto;
import com.sendback.domain.project.dto.response.ProjectDetailResponseDto;
import com.sendback.domain.project.dto.response.ProjectIdResponseDto;
import com.sendback.domain.project.entity.Project;
import com.sendback.domain.project.entity.ProjectImage;
import com.sendback.domain.project.repository.ProjectImageRepository;
import com.sendback.domain.project.repository.ProjectRepository;
import com.sendback.domain.scrap.repository.ScrapRepository;
import com.sendback.domain.user.entity.User;
import com.sendback.domain.user.service.UserService;
import com.sendback.global.ServiceTest;
import com.sendback.global.config.image.service.ImageService;
import com.sendback.global.exception.type.BadRequestException;
import com.sendback.global.exception.type.NotFoundException;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.sendback.domain.project.exception.ProjectExceptionType.*;
import static com.sendback.domain.project.fixture.ProjectFixture.*;
import static com.sendback.domain.user.fixture.UserFixture.createDummyUser;
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
           assertThat(response.username()).isEqualTo(user.getNickname());
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
            assertThat(response.username()).isEqualTo(user.getNickname());
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

}
