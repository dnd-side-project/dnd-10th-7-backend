package com.sendback.domain.feedback.dto.response;

public record SubmitFeedbackResponse(
        String level,
        boolean isLevelUp,
        Long remainFeedbackCount
) {}
