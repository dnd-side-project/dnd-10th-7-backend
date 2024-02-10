package com.sendback.domain.project.controller;

import com.sendback.domain.project.dto.request.SaveProjectRequest;
import com.sendback.domain.project.dto.request.UpdateProjectRequest;
import com.sendback.domain.project.service.ProjectService;
import com.sendback.global.common.ApiResponse;
import com.sendback.global.common.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.sendback.global.common.ApiResponse.success;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ApiResponse<Long> saveProject(
            @UserId Long userId,
            @RequestPart(value = "data") SaveProjectRequest saveProjectRequest,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        return success(projectService.saveProject(userId, saveProjectRequest, images));
    }

    @PutMapping("{projectId}")
    public ApiResponse<Long> updateProject(
            @UserId Long userId,
            @PathVariable Long projectId,
            @RequestPart(value = "data") UpdateProjectRequest updateProjectRequest,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        return success(projectService.updateProject(userId, projectId, updateProjectRequest, images));
    }

    @DeleteMapping("{projectId}")
    public Void deleteProject(
            @UserId Long userId,
            @PathVariable Long projectId) {

        projectService.deleteProject(userId, projectId);

        return null;
    }
}
