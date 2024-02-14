package com.sendback.domain.feedback.dto.request;

import java.time.LocalDate;

public record SaveFeedbackRequest(
        String title,
        String linkUrl,
        String content,
        String rewardMessage,
        LocalDate startedAt,
        LocalDate endedAt
) {}
