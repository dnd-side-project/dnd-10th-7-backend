package com.sendback.domain.user.dto;

public record SigningAccount(
        String socialId,
        String socialname,
        String profileImageUrl,
        String email,
        String socialType
)
{}
