package com.sendback.domain.feedback.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sendback.domain.feedback.entity.Feedback;

import java.time.LocalDate;

public record FeedbackResponseDto(
        Long feedbackId,
        String title,
        String rewardMessage,
        @JsonFormat(pattern = "yyyy.MM.dd")
        LocalDate startedAt,
        @JsonFormat(pattern = "yyyy.MM.dd")
        LocalDate endedAt,
        boolean isFinished,
        boolean isAuthor,
        boolean isSubmitted
) {
    public static FeedbackResponseDto of(Feedback feedback, boolean isAuthor, boolean isSubmitted) {
        return new FeedbackResponseDto(
                feedback.getId(),
                feedback.getTitle(),
                feedback.getRewardMessage(),
                feedback.getStartedAt(),
                feedback.getEndedAt(),
                feedback.isFinished(),
                isAuthor,
                isSubmitted
        );
    }
}