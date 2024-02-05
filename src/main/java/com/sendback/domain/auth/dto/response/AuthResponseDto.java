package com.sendback.domain.auth.dto.response;

public class AuthResponseDto{
    public record TokensResponseDto(String accessToken, String refreshToken){}
}
