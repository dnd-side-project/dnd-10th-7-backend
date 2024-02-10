package com.sendback.domain.project.dto.request;

import java.time.LocalDate;
import java.util.List;

public record UpdateProjectRequest(
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
        Long designCount,
        List<String> urlsToDelete
) {

}
