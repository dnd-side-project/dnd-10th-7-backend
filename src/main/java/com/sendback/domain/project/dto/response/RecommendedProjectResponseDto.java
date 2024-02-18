package com.sendback.domain.project.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import com.sendback.domain.project.entity.Progress;
import com.sendback.global.common.constants.FieldName;

import java.time.LocalDateTime;

public record RecommendedProjectResponseDto (
    Long projectId,
    String progress,
    String field,
    String title,
    String summary,
    String createdBy,
    @JsonFormat(pattern = "yyyy.MM.dd")
    LocalDateTime createdAt,

    String profileImageUrl
){
    @QueryProjection
    public RecommendedProjectResponseDto(
            Long projectId,
            String progress,
            String field,
            String title,
            String summary,
            String createdBy,
            LocalDateTime createdAt,
            String profileImageUrl
    ) {
        this.projectId = projectId;
        this.progress = Progress.toKorean(progress);
        this.field = FieldName.toKorean(field);
        this.title = title;
        this.summary = summary;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.profileImageUrl = profileImageUrl;
    }
}