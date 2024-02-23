package com.sendback.domain.user.dto;

public record SigningUser(
        String socialId,
        String socialname,
        String profileImageUrl,
        String email,
        String socialType
)
{}
