package com.sendback.domain.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sendback.domain.auth.dto.Token;
import com.sendback.domain.auth.dto.response.TokensResponseDto;
import com.sendback.domain.auth.service.AuthService;
import com.sendback.domain.auth.service.GoogleService;
import com.sendback.domain.auth.service.KakaoService;
import com.sendback.domain.user.dto.request.SignUpRequestDto;
import com.sendback.domain.user.service.UserService;
import com.sendback.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ApiResponse<Token> signUpUser(@RequestBody SignUpRequestDto signUpRequestDto) {
        Token tokens = userService.signUpUser(signUpRequestDto);
        return ApiResponse.success(tokens);
    }

}
