package com.sendback.domain.project.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record UpdateProjectRequest(

        @Size(max = 30, message = "제목은 글자 수가 최대 30글자 입니다.")
        @NotBlank(message = "제목은 비워둘 수 없습니다.")
        String title,
        @NotBlank(message = "분야는 비워둘 수 없습니다.")
        String field,
        @Size(max = 1_000, message = "설명은 글자 수가 최대 1,000자 입니다.")
        @NotBlank(message = "설명은 비워둘 수 없습니다.")
        String content,
        @Size(max = 50, message = "한 줄 요약은 글자 수가 최대 50글자 입니다.")
        @NotBlank(message = "한 줄 요약은 비워둘 수 없습니다.")
        String summary,
        String demoSiteUrl,
        @NotNull(message = "시작 날짜는 비워둘 수 없습니다.")
        LocalDate startedAt,
        @NotNull(message = "끝나는 날짜는 비워둘 수 없습니다.")
        LocalDate endedAt,
        @NotBlank(message = "진행 상황은 비워둘 수 없습니다.")
        String progress,
        Long plannerCount,
        Long frontendCount,
        Long backendCount,
        Long designCount,
        @NotNull(message = "삭제할 이미지 리스트는 빈 리스트라도 보내야합니다.")
        List<String> urlsToDelete
) {

}
