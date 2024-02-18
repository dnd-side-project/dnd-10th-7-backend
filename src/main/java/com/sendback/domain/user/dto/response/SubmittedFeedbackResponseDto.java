package com.sendback.domain.user.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import com.sendback.domain.project.entity.Progress;
import com.sendback.global.common.constants.FieldName;
import java.time.LocalDateTime;

public record SubmittedFeedbackResponseDto (

    String progress,
    String field,
    String title,
    String summary,
    Long feedbackId,
    @JsonFormat(pattern = "yyyy.MM.dd")
    LocalDateTime createdAt
){
    @QueryProjection
    public SubmittedFeedbackResponseDto(
            String progress,
            String field,
            String title,
            String summary,
            Long feedbackId,
            LocalDateTime createdAt
    ) {
        this.progress = Progress.toKorean(progress);
        this.field = FieldName.toKorean(field);
        this.title = title;
        this.summary = summary;
        this.feedbackId = feedbackId;
        this.createdAt = createdAt;
    }
}





