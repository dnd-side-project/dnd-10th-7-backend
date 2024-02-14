package com.sendback.domain.user.dto;

public record SigningAccount(
        String socialId,
        String nickname,
        String profileImageUrl,
        String email,
        String socialType
)
{}
