package com.sendback.domain.user.dto.request;

import jakarta.validation.constraints.*;

import java.util.List;

public record SignUpRequestDto(
        @Size(min = 2, max = 8, message = "닉네임은 2자 이상 8자 이하여야 합니다.")
        @Pattern(regexp = "^[a-zA-Z가-힣]{2,8}$", message = "닉네임은 한글 또는 영어로만 이루어져야 합니다.")
        String nickname,

        @Pattern(regexp = "\\d{4}\\.\\d{2}\\.\\d{2}", message = "생년월일은 'yyyy.MM.dd' 형식이어야 합니다.")
        String birthday,

        @Pattern(regexp = "남자|여자", message = "성별은 필수입니다.")
        String gender,

        @Pattern(regexp = "프론트엔드|백엔드|디자이너|기획자", message = "직업군은 '프론트엔드', '백엔드', '디자이너', '기획자' 중 하나여야 합니다.")
        String career,

        @Size(min = 1, message = "분야는 최소 1개 이상 선택되어야 합니다.")
        List<@Pattern(regexp = "예술/대중문화|환경|건강|취미/실용|금융/핀테크|교육|게임|AI/머신러닝",
                message = "분야는 '예술/대중문화', '환경', '건강', '취미/실용', '금융/핀테크', '교육', '게임', 'AI/머신러닝' 중 하나여야 합니다.") String>
                fields,

        @NotBlank(message = "sign token이 필요합니다.")
        String signToken
)
{}
