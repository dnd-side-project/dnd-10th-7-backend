package com.sendback.domain.feedback.dto.response;

public record SubmitFeedbackResponseDto(
        String level,
        boolean isLevelUp,
        Long remainFeedbackCount
) {}
