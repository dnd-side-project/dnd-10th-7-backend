package com.sendback.domain.feedback.dto.response;

import java.util.List;

public record GetFeedbacksResponse(
        List<FeedbackResponseDto> feedbacks,
        boolean isAuthor
) {}