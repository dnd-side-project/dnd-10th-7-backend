package com.sendback.domain.feedback.dto.response;

import com.sendback.domain.feedback.entity.Feedback;

public record FeedbackResponseDto(
        Long feedbackId,
        String title,
        String rewardMessage,
        String startedAt,
        String endedAt,
        boolean isFinished,
        boolean isAuthor,
        boolean isSubmitted
) {
    public static FeedbackResponseDto of(Feedback feedback, boolean isAuthor, boolean isSubmitted) {
        return new FeedbackResponseDto(
                feedback.getId(),
                feedback.getTitle(),
                feedback.getRewardMessage(),
                feedback.getStartedAt().toString(),
                feedback.getEndedAt().toString(),
                feedback.isFinished(),
                isAuthor,
                isSubmitted
        );
    }
}