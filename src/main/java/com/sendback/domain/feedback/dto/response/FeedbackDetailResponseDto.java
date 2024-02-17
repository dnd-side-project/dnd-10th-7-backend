package com.sendback.domain.feedback.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sendback.domain.feedback.entity.Feedback;
import com.sendback.domain.project.entity.Project;
import com.sendback.domain.user.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record FeedbackDetailResponseDto(

        Long userId,
        String username,
        String userLevel,
        String profileImageUrl,
        Long feedbackId,
        String feedbackTitle,
        String linkUrl,
        String content,
        String rewardMessage,
        @JsonFormat(pattern = "yyyy.MM.dd hh:mm")
        LocalDateTime createdAt,
        @JsonFormat(pattern = "yyyy.MM.dd")
        LocalDate startedAt,
        @JsonFormat(pattern = "yyyy.MM.dd")
        LocalDate endedAt,
        Long projectId,
        String projectTitle,
        String fieldName,
        String progress
) {
    public static FeedbackDetailResponseDto from(Feedback feedback) {
        Project project = feedback.getProject();
        User user = project.getUser();
        return new FeedbackDetailResponseDto(
                user.getId(),
                user.getNickname(),
                user.getLevel().getName(),
                user.getProfileImageUrl(),
                feedback.getId(),
                feedback.getTitle(),
                feedback.getLinkUrl(),
                feedback.getContent(),
                feedback.getRewardMessage(),
                feedback.getCreatedAt(),
                feedback.getStartedAt(),
                feedback.getEndedAt(),
                project.getId(),
                project.getTitle(),
                project.getFieldName().getName(),
                project.getProgress().toString()
        );
    }
}
