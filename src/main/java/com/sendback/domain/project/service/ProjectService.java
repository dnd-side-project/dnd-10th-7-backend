package com.sendback.domain.project.service;

import com.sendback.domain.field.entity.Field;
import com.sendback.domain.field.service.FieldService;
import com.sendback.domain.project.dto.request.SaveProjectRequestDto;
import com.sendback.domain.project.dto.request.UpdateProjectRequestDto;
import com.sendback.domain.project.dto.response.ProjectIdResponseDto;
import com.sendback.domain.project.entity.Project;
import com.sendback.domain.project.entity.ProjectImage;
import com.sendback.domain.project.repository.ProjectImageRepository;
import com.sendback.domain.project.repository.ProjectRepository;
import com.sendback.domain.user.entity.User;
import com.sendback.domain.user.service.UserService;
import com.sendback.global.config.image.service.ImageService;
import com.sendback.global.exception.type.BadRequestException;
import com.sendback.global.exception.type.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.sendback.domain.project.exception.ProjectExceptionType.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {

    private final UserService userService;
    private final FieldService fieldService;
    private final ImageService imageService;
    private final ProjectRepository projectRepository;
    private final ProjectImageRepository projectImageRepository;

    @Transactional
    public ProjectIdResponseDto saveProject(Long userId, SaveProjectRequestDto saveProjectRequestDto, List<MultipartFile> images) {
        User loginUser = userService.getUserById(userId);

        Field field = fieldService.getFieldByName(saveProjectRequestDto.field());
        Project project = Project.of(loginUser, field, saveProjectRequestDto);

        uploadProjectImage(project, images);

        return new ProjectIdResponseDto(projectRepository.save(project).getId());
    }

    private void uploadProjectImage(Project project, List<MultipartFile> images) {
        if (images == null)
            return;

        if (images.size() > 5) {
            throw new BadRequestException(IMAGE_SIZE_OVER);
        }

        List<String> imageUrls = imageService.upload(images, "project");
        imageUrls.forEach(image -> projectImageRepository.save(ProjectImage.of(project, image)));
    }

    @Transactional
    public ProjectIdResponseDto updateProject(Long userId, Long projectId, UpdateProjectRequestDto updateProjectRequestDto, List<MultipartFile> images) {
        User loginUser = userService.getUserById(userId);
        Field field = fieldService.getFieldByName(updateProjectRequestDto.field());
        Project project = getProjectById(projectId);

        validateProjectAuthor(loginUser, project);
        validateProjectImageCount(project, images, updateProjectRequestDto.urlsToDelete());

        deleteImageUrls(project, updateProjectRequestDto.urlsToDelete());
        uploadProjectImage(project, images);
        project.updateProject(field, updateProjectRequestDto);

        return new ProjectIdResponseDto(project.getId());
    }

    private void validateProjectImageCount(Project project, List<MultipartFile> images, List<String> urlsToDelete) {
        List<ProjectImage> projectImages = projectImageRepository.findAllByProject(project);

        int imageSize = (images == null ? 0 : images.size());

        if (projectImages.size() + imageSize - urlsToDelete.size() > 5) {
            throw new BadRequestException(IMAGE_SIZE_OVER);
        }
    }

    @Transactional
    public void deleteProject(Long userId, Long projectId) {
        User loginUser = userService.getUserById(userId);
        Project project = getProjectById(projectId);

        validateProjectAuthor(loginUser, project);

        List<ProjectImage> projectImages = projectImageRepository.findAllByProject(project);

        // 추후 연관된 것 모두 삭제 처리할 것
        projectImageRepository.deleteAll(projectImages);
        projectRepository.delete(project);
    }

    private void deleteImageUrls(Project project, List<String> urlsToDelete) {
        if (!urlsToDelete.isEmpty()){
            urlsToDelete.forEach(url -> {
                ProjectImage projectImage = getProjectImageByProjectAndImageUrl(project, url);
                projectImageRepository.delete(projectImage);
            });
        }
    }

    private ProjectImage getProjectImageByProjectAndImageUrl(Project project, String url) {
        return projectImageRepository.findByProjectAndImageUrl(project, url)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_DELETE_IMAGE_URL));
    }

    public void validateProjectAuthor(User user, Project project) {
        if (!project.isAuthor(user))
            throw new BadRequestException(NOT_PROJECT_AUTHOR);
    }

    public Project getProjectById(Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new NotFoundException(NOT_FOUND_PROJECT));
        if (project.isDeleted())
            throw new BadRequestException(DELETED_PROJECT);
        return project;
    }
}
