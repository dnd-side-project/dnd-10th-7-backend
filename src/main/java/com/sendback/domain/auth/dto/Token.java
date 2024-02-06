package com.sendback.domain.auth.dto;

public record Token (
        String accessToken,
        String refreshToken
) {

}
