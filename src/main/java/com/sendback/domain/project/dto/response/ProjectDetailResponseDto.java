package com.sendback.domain.project.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sendback.domain.project.entity.Project;
import com.sendback.domain.project.entity.ProjectImage;
import com.sendback.domain.user.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record ProjectDetailResponseDto(
        Long userId,
        String nickname,
        String userLevel,
        String profileImageUrl,
        Long projectId,
        String title,
        String field,
        String content,
        String demoSiteUrl,
        String progress,
        List<String> projectImageUrl,
        Long frontendCount,
        Long backendCount,
        Long designerCount,
        Long plannerCount,
        Long likeCount,
        Long scrapCount,
        Long commentCount,
        @JsonFormat(pattern = "yyyy.MM.dd hh:mm")
        LocalDateTime createdAt,
        @JsonFormat(pattern = "yyyy.MM.dd")
        LocalDate startedAt,
        @JsonFormat(pattern = "yyyy.MM.dd")
        LocalDate endedAt,
        boolean isAuthor,
        boolean isCheckedLike,
        boolean isCheckedScrap
) {
    public static ProjectDetailResponseDto of(Project project, boolean isAuthor, boolean isCheckedLike, boolean isCheckedScrap) {
        User user = project.getUser();
        return new ProjectDetailResponseDto(
                user.getId(),
                user.getNickname(),
                user.getLevel().getName(),
                user.getProfileImageUrl(),
                project.getId(),
                project.getTitle(),
                project.getFieldName().getName(),
                project.getContent(),
                project.getDemoSiteUrl(),
                project.getProgress().getValue(),
                project.getProjectImages().stream().map(ProjectImage::getImageUrl).toList(),
                project.getProjectParticipantCount().getFrontendCount(),
                project.getProjectParticipantCount().getBackendCount(),
                project.getProjectParticipantCount().getDesignCount(),
                project.getProjectParticipantCount().getPlannerCount(),
                project.getLikes().stream().filter(like -> !like.isDeleted()).count(),
                project.getScraps().stream().filter(scrap -> !scrap.isDeleted()).count(),
                project.getComments().stream().filter(comment -> !comment.isDeleted()).count(),
                project.getCreatedAt(),
                project.getStartedAt(),
                project.getEndedAt(),
                isAuthor,
                isCheckedLike,
                isCheckedScrap
        );
    }
}