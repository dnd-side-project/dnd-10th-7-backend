package com.sendback.domain.project.dto.request;

import java.time.LocalDate;

public record SaveProjectRequest(
        String title,
        String field,
        String content,
        String demoSiteUrl,
        LocalDate startedAt,
        LocalDate endedAt,
        String progress,
        Long plannerCount,
        Long frontendCount,
        Long backendCount,
        Long designCount
        ) {}
