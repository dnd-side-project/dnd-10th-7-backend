package com.sendback.domain.user.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import com.sendback.domain.project.entity.Progress;
import com.sendback.global.common.constants.FieldName;
import java.time.LocalDateTime;

public record ScrappedProjectResponseDto (
        Long projectId,
        String title,
        String progress,
        String summary,
        String field,
        @JsonFormat(pattern = "yyyy.MM.dd")
        LocalDateTime createdAt,
        Long pullUpCnt
) {
    @QueryProjection
    public ScrappedProjectResponseDto(
            Long projectId,
            String title,
            String progress,
            String summary,
            String field,
            LocalDateTime createdAt,
            Long pullUpCnt
    ) {
        this.projectId = projectId;
        this.title = title;
        this.progress = Progress.toKorean(progress);
        this.summary = summary;
        this.field = FieldName.toKorean(field);
        this.createdAt = createdAt;
        this.pullUpCnt = pullUpCnt;
    }
}