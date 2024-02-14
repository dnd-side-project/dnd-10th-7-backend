package com.sendback.domain.project.controller;

import com.sendback.domain.project.dto.request.SaveProjectRequestDto;
import com.sendback.domain.project.dto.request.UpdateProjectRequestDto;
import com.sendback.domain.project.dto.response.ProjectIdResponseDto;
import com.sendback.domain.project.service.ProjectService;
import com.sendback.global.common.ApiResponse;
import com.sendback.global.common.UserId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.sendback.global.common.ApiResponse.success;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ApiResponse<ProjectIdResponseDto> saveProject(
            @UserId Long userId,
            @RequestPart(value = "data") @Valid SaveProjectRequestDto saveProjectRequestDto,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        return success(projectService.saveProject(userId, saveProjectRequestDto, images));
    }

    @PutMapping(value = "/{projectId}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ApiResponse<ProjectIdResponseDto> updateProject(
            @UserId Long userId,
            @PathVariable Long projectId,
            @RequestPart(value = "data") @Valid UpdateProjectRequestDto updateProjectRequestDto,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        return success(projectService.updateProject(userId, projectId, updateProjectRequestDto, images));
    }

    @DeleteMapping("/{projectId}")
    public ApiResponse<Object> deleteProject(
            @UserId Long userId,
            @PathVariable Long projectId) {

        projectService.deleteProject(userId, projectId);

        return success(null);
    }
}
