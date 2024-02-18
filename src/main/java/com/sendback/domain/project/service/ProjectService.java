package com.sendback.domain.project.service;

import com.sendback.domain.field.entity.Field;
import com.sendback.domain.field.repository.FieldRepository;
import com.sendback.domain.like.repository.LikeRepository;
import com.sendback.domain.project.dto.request.SaveProjectRequestDto;
import com.sendback.domain.project.dto.request.UpdateProjectRequestDto;
import com.sendback.domain.project.dto.response.ProjectDetailResponseDto;
import com.sendback.domain.project.dto.response.ProjectIdResponseDto;
import com.sendback.domain.project.dto.response.RecommendedProjectResponseDto;
import com.sendback.domain.project.entity.Project;
import com.sendback.domain.project.entity.ProjectImage;
import com.sendback.domain.project.repository.ProjectImageRepository;
import com.sendback.domain.project.repository.ProjectRepository;
import com.sendback.domain.scrap.repository.ScrapRepository;
import com.sendback.domain.user.entity.User;
import com.sendback.domain.user.service.UserService;
import com.sendback.global.common.constants.FieldName;
import com.sendback.global.config.image.service.ImageService;
import com.sendback.global.exception.type.BadRequestException;
import com.sendback.global.exception.type.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import static com.sendback.domain.project.exception.ProjectExceptionType.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {

    private final UserService userService;
    private final ImageService imageService;
    private final ProjectRepository projectRepository;
    private final ProjectImageRepository projectImageRepository;
    private final LikeRepository likeRepository;
    private final ScrapRepository scrapRepository;

    private final FieldRepository fieldRepository;

    public ProjectDetailResponseDto getProjectDetail(Long userId, Long projectId) {
        Project project = getProjectById(projectId);

        if (userId == null) {
            return ProjectDetailResponseDto.of(project, false, false, false);
        }

        User loginUser = userService.getUserById(userId);
        boolean isAuthor = checkAuthor(loginUser, project);
        boolean isCheckedLike = checkLike(loginUser, project);
        boolean isCheckedScrap = checkScrap(loginUser, project);


        return ProjectDetailResponseDto.of(project, isAuthor, isCheckedLike, isCheckedScrap);
    }

    private boolean checkScrap(User user, Project project) {
        return scrapRepository.existsByUserAndProjectAndIsDeletedIsFalse(user, project);
    }

    private boolean checkLike(User user, Project project) {
        return likeRepository.existsByUserAndProjectAndIsDeletedIsFalse(user, project);
    }

    @Transactional
    public ProjectIdResponseDto saveProject(Long userId, SaveProjectRequestDto saveProjectRequestDto, List<MultipartFile> images) {
        User loginUser = userService.getUserById(userId);

        Project project = Project.of(loginUser, saveProjectRequestDto);

        uploadProjectImage(project, images);

        return new ProjectIdResponseDto(projectRepository.save(project).getId());
    }

    @Transactional
    public ProjectIdResponseDto updateProject(Long userId, Long projectId, UpdateProjectRequestDto updateProjectRequestDto, List<MultipartFile> images) {
        User loginUser = userService.getUserById(userId);
        FieldName fieldName = FieldName.toEnum(updateProjectRequestDto.fieldName());
        Project project = getProjectById(projectId);

        validateProjectAuthor(loginUser, project);
        validateProjectImageCount(project, images, updateProjectRequestDto.urlsToDelete());

        deleteImageUrls(project, updateProjectRequestDto.urlsToDelete());
        uploadProjectImage(project, images);
        project.updateProject(fieldName, updateProjectRequestDto);

        return new ProjectIdResponseDto(project.getId());
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

    public List<RecommendedProjectResponseDto> getRecommendedProject(Long userId){
        List<FieldName> fieldNameList = new ArrayList<>();
        if(userId!=null) {
            List<Field> fieldList = fieldRepository.findAllByUserId(userId);
            fieldNameList = fieldList.stream().map(Field::getName).collect(Collectors.toList());
        }
        return projectRepository.findRecommendedProjects(userId, fieldNameList);
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

    private void uploadProjectImage(Project project, List<MultipartFile> images) {
        if (images == null)
            return;

        if (images.size() > 5) {
            throw new BadRequestException(IMAGE_SIZE_OVER);
        }

        List<String> imageUrls = imageService.upload(images, "project");
        imageUrls.forEach(image -> projectImageRepository.save(ProjectImage.of(project, image)));
    }

    private void validateProjectImageCount(Project project, List<MultipartFile> images, List<String> urlsToDelete) {
        List<ProjectImage> projectImages = projectImageRepository.findAllByProject(project);

        int imageSize = (images == null ? 0 : images.size());

        if (projectImages.size() + imageSize - urlsToDelete.size() > 5) {
            throw new BadRequestException(IMAGE_SIZE_OVER);
        }
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

    private boolean checkAuthor(User user, Project project) {
        return project.isAuthor(user);
    }
}
