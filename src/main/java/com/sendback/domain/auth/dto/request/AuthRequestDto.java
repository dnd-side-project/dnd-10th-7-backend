package com.sendback.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public class AuthRequestDto{
    public record RefreshTokenRequestDto(String refreshToken){}

}

