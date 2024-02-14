package com.sendback.domain.feedback.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record SaveFeedbackRequest(
        @Size(max = 30, message = "제목은 글자 수가 최대 30글자 입니다.")
        @NotBlank(message = "제목은 비워둘 수 없습니다.")
        String title,
        @NotBlank
        String linkUrl,
        @Size(max = 500, message = "설명은 글자 수가 최대 500자 입니다.")
        @NotBlank(message = "설명은 비워둘 수 없습니다.")
        String content,
        @Size(max = 100, message = "추가 리워드는 글자 수가 최대 100자 입니다.")
        String rewardMessage,
        @NotNull(message = "시작 날짜는 비워둘 수 없습니다.")
        LocalDate startedAt,
        @NotNull(message = "끝나는 날짜는 비워둘 수 없습니다.")
        LocalDate endedAt
) {}
