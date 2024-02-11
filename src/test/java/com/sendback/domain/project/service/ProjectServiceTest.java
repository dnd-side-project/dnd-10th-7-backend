package com.sendback.domain.project.service;

import com.sendback.domain.field.entity.Field;
import com.sendback.domain.field.service.FieldService;
import com.sendback.domain.project.dto.request.SaveProjectRequest;
import com.sendback.domain.project.dto.request.UpdateProjectRequest;
import com.sendback.domain.project.entity.Project;
import com.sendback.domain.project.entity.ProjectImage;
import com.sendback.domain.project.repository.ProjectImageRepository;
import com.sendback.domain.project.repository.ProjectRepository;
import com.sendback.domain.user.entity.User;
import com.sendback.domain.user.service.UserService;
import com.sendback.global.ServiceTest;
import com.sendback.global.config.image.service.ImageService;
import com.sendback.global.exception.type.BadRequestException;
import com.sendback.global.exception.type.ImageException;
import com.sendback.global.exception.type.NotFoundException;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.sendback.domain.project.exception.ProjectExceptionType.*;
import static com.sendback.domain.project.fixture.ProjectFixture.*;
import static com.sendback.domain.user.fixture.UserFixture.createDummyUser;
import static com.sendback.global.config.image.exception.ImageExceptionType.AWS_S3_UPLOAD_FAIL;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


public class ProjectServiceTest extends ServiceTest {

    @InjectMocks
    ProjectService projectService;

    @Mock
    UserService userService;
    @Mock
    FieldService fieldService;
    @Mock
    ImageService imageService;
    @Mock
    ProjectImageRepository projectImageRepository;
    @Mock
    ProjectRepository projectRepository;

    private User user;
    private Project project;
    private final List<MultipartFile> images = List.of(mockingMultipartFile("sendback1.jpg"),
            mockingMultipartFile("sendback2.jpg"), mockingMultipartFile("sendback3.jpg"));
    private final Field eduField = Field.of("edu");
    private final static Long SPYING_PROJECT_ID = 1L;
    private final SaveProjectRequest saveProjectRequest = mock_saveProjectRequest;
    private final UpdateProjectRequest updateProjectRequest = mock_updateProjectRequest;

    @BeforeEach
    public void setUp() {
        this.user = spy(createDummyUser());
        this.project = spy(createDummyProject(user));
    }

    @Test
    @DisplayName("정상적인 요청 시 프로젝트를 생성한다. (이미지가 존재하지 않을 때)")
    public void saveProject_success() throws Exception {
        //given
        given(userService.getUserById(any())).willReturn(user);
        given(fieldService.getFieldByName(saveProjectRequest.field())).willReturn(eduField);
        given(projectRepository.save(any())).willReturn(project);
        when(project.getId()).thenReturn(SPYING_PROJECT_ID);

        //when
        Long response = projectService.saveProject(1L, saveProjectRequest, List.of());

        //then
        assertThat(response).isEqualTo(1L);
    }

    @Test
    @DisplayName("정상적인 요청 시 프로젝트를 생성한다. (이미지가 존재할 때)")
    public void saveProjectContainImage_success() throws Exception {
        //given
        given(userService.getUserById(any())).willReturn(user);
        given(fieldService.getFieldByName(saveProjectRequest.field())).willReturn(eduField);
        given(imageService.upload(images, "project")).willReturn(List.of("sendback1.jpg", "sendback2.jpg", "sendback3.jpg"));
        given(projectRepository.save(any())).willReturn(project);
        when(project.getId()).thenReturn(SPYING_PROJECT_ID);


        //when
        Long response = projectService.saveProject(1L, saveProjectRequest, images);

        //then
        assertThat(response).isEqualTo(1L);
        verify(projectImageRepository, times(3)).save(any());
    }

    @Nested
    @DisplayName("프로젝트 수정 시")
    class UpdateProject {
        @Test
        @DisplayName("프로젝트를 찾을 수 없으면 예외를 발생한다.")
        public void fail_notFoundProject() throws Exception {
            //given
            UpdateProjectRequest updateProjectRequest = new UpdateProjectRequest(project.getTitle(), project.getField().toString(), project.getContent(), project.getDemoSiteUrl(),
                    project.getStartedAt(), project.getEndedAt(),
                    project.getProgress().toString(), project.getProjectParticipantCount().getPlannerCount(), project.getProjectParticipantCount().getFrontendCount(),
                    project.getProjectParticipantCount().getBackendCount(), project.getProjectParticipantCount().getDesignCount(), List.of());
            given(userService.getUserById(anyLong())).willReturn(user);
            given(fieldService.getFieldByName(updateProjectRequest.field())).willReturn(eduField);
            given(projectRepository.findById(anyLong())).willReturn(Optional.empty());

            //when - then
            assertThatThrownBy(() -> projectService.updateProject(1L, 1L, updateProjectRequest, List.of()))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage(NOT_FOUND_PROJECT.getMessage());
        }

        @Test
        @DisplayName("글 작성자와 수정자가 다르면 오류를 발생한다.")
        public void fail_notAuthor() throws Exception {
            //given
            given(userService.getUserById(anyLong())).willReturn(user);
            given(fieldService.getFieldByName(updateProjectRequest.field())).willReturn(eduField);
            given(projectRepository.findById(anyLong())).willReturn(Optional.of(project));
            given(project.isAuthor(user)).willReturn(false);

            //when - then
            assertThatThrownBy(() -> projectService.updateProject(1L, 1L, updateProjectRequest, List.of()))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage(NOT_PROJECT_AUTHOR.getMessage());
        }

        @Test
        @DisplayName("삭제 요청온 url이 올바르지 않으면 오류를 발생한다.")
        public void fail_notFoundDeleteUrls() throws Exception {
            //given
            given(userService.getUserById(anyLong())).willReturn(user);
            given(fieldService.getFieldByName(updateProjectRequest.field())).willReturn(eduField);
            given(projectRepository.findById(anyLong())).willReturn(Optional.of(project));
            given(project.isAuthor(user)).willReturn(true);
            given(projectImageRepository.findByProjectAndImageUrl(any(Project.class), anyString())).willReturn(Optional.empty());

            //when - then
            assertThatThrownBy(() -> projectService.updateProject(1L, 1L, updateProjectRequest, List.of()))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage(NOT_FOUND_DELETE_IMAGE_URL.getMessage());
        }

        @Test
        @DisplayName("정상적인 요청 시 프로젝트를 수정한다.")
        public void success() throws Exception {
            //given
            ProjectImage projectImage = ProjectImage.of(project, "url");
            given(userService.getUserById(anyLong())).willReturn(user);
            given(fieldService.getFieldByName(updateProjectRequest.field())).willReturn(eduField);
            given(projectRepository.findById(anyLong())).willReturn(Optional.of(project));
            given(project.isAuthor(user)).willReturn(true);
            given(projectImageRepository.findByProjectAndImageUrl(any(Project.class), anyString())).willReturn(Optional.of(projectImage));
            doNothing().when(projectImageRepository).delete(any(ProjectImage.class));
            when(project.getId()).thenReturn(SPYING_PROJECT_ID);

            //when
            Long response = projectService.updateProject(1L, 1L, updateProjectRequest, images);

            //then
            assertThat(response).isEqualTo(1L);
            assertThat(project.getTitle()).isEqualTo(updateProjectRequest.title());
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
    }

    private MultipartFile mockingMultipartFile(String fileName) {
        return new MockMultipartFile(
                "images",
                fileName,
                MediaType.IMAGE_JPEG_VALUE,
                generateMockImage()
        );
    }

    private byte[] generateMockImage() {
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            ImageIO.write(image, "jpg", byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new ImageException(AWS_S3_UPLOAD_FAIL);
        }
    }

}
