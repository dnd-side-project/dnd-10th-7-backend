package com.sendback.domain.project.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sendback.domain.project.entity.Project;

import java.time.LocalDateTime;

public record GetProjectsResponseDto(
        String nickname,
        String profileImageUrl,
        Long projectId,
        String title,
        String summary,
        String progress,
        String field,
        @JsonFormat(pattern = "yyyy.MM.dd")
        LocalDateTime createdAt,
        Long pullUpCount,
        Long likeCount,
        Long commentCount,
        boolean isScrapped
) {
    public static GetProjectsResponseDto of(Project project, boolean isScrapped) {
        return new GetProjectsResponseDto(
                project.getUser().getNickname(),
                project.getUser().getProfileImageUrl(),
                project.getId(),
                project.getTitle(),
                project.getSummary(),
                project.getProgress().getValue(),
                project.getFieldName().getName(),
                project.getCreatedAt(),
                project.getProjectPull().getPullUpCnt(),
                project.getLikes().stream().map(like -> !like.isDeleted()).count(),
                project.getComments().stream().map(comment -> !comment.isDeleted()).count(),
                isScrapped
        );
    }
}
