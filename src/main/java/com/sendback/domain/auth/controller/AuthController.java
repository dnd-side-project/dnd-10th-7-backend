package com.sendback.domain.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sendback.domain.auth.dto.Token;
import com.sendback.domain.auth.dto.request.AuthRequestDto.*;
import com.sendback.domain.auth.dto.response.AuthResponseDto.TokensResponseDto;
import com.sendback.domain.auth.service.AuthService;
import com.sendback.domain.auth.service.GoogleService;
import com.sendback.domain.auth.service.KakaoService;
import com.sendback.global.common.ApiResponse;
import com.sendback.global.config.auth.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/auth")
public class AuthController {

    private final KakaoService kakaoService;
    private final GoogleService googleService;
    private final AuthService authService;

    @GetMapping("/kakao/callback")
    public ApiResponse<TokensResponseDto> loginKakao(@RequestParam String code) throws JsonProcessingException {
        TokensResponseDto tokens = kakaoService.loginKakao(code);
        return ApiResponse.success(tokens);
    }

    @GetMapping("/google/callback")
    public ApiResponse<TokensResponseDto> loginGoogle(@RequestParam String code) throws JsonProcessingException {
        TokensResponseDto tokens = googleService.loginGoogle(code);
        return ApiResponse.success(tokens);
    }

}
