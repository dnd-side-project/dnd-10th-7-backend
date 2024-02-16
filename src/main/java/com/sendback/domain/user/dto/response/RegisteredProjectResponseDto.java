package com.sendback.domain.user.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import java.util.List;

public record RegisteredProjectResponseDto (
    Long projectId,
    String title,
    String progress,
    String summary,

    @JsonFormat(pattern = "yyyy.MM.dd")
    LocalDateTime createdAt,
    Long pullUpCnt
){
    @QueryProjection
    public RegisteredProjectResponseDto(
            Long projectId,
            String title,
            String progress,
            String summary,
            LocalDateTime createdAt,
            Long pullUpCnt
    ) {
        this.projectId = projectId;
        this.title = title;
        this.progress = progress;
        this.summary = summary;
        this.createdAt = createdAt;
        this.pullUpCnt = pullUpCnt;
    }
}
