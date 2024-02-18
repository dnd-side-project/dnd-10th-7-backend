package com.sendback.domain.user.dto.response;
import java.util.List;

public record UpdateUserInfoResponseDto(
        String nickname,
        String birthday,
        String career,
        List<String> fields
)
{}
