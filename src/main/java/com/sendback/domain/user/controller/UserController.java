package com.sendback.domain.user.controller;

import com.sendback.domain.auth.dto.Token;
import com.sendback.domain.user.dto.response.CheckUserNicknameResponseDto;
import com.sendback.domain.user.dto.request.SignUpRequestDto;
import com.sendback.domain.user.service.UserService;
import com.sendback.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ApiResponse<Token> signUpUser(@RequestBody @Valid SignUpRequestDto signUpRequestDto) {
        Token tokens = userService.signUpUser(signUpRequestDto);
        return ApiResponse.success(tokens);
    }

    @GetMapping("/check")
    public ApiResponse<CheckUserNicknameResponseDto> checkUserNickname(@RequestParam String nickname) {
        return ApiResponse.success(userService.checkUserNickname(nickname));
    }
}
