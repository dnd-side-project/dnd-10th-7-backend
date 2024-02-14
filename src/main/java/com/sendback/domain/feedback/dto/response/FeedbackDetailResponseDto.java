package com.sendback.domain.feedback.dto.response;

import com.sendback.domain.feedback.entity.Feedback;
import com.sendback.domain.project.entity.Project;
import com.sendback.domain.user.entity.User;

import static com.sendback.global.util.CustomDateUtil.customDateFormat;

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
        String createdAt,
        String startedAt,
        String endedAt,
        Long projectId,
        String projectTitle,
        String field,
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
                customDateFormat(feedback.getCreatedAt()),
                feedback.getStartedAt().toString(),
                feedback.getEndedAt().toString(),
                project.getId(),
                project.getTitle(),
                project.getField().getName(),
                project.getProgress().toString()
        );
    }
}
