package com.sendback.domain.user.controller;

import com.sendback.domain.user.dto.request.SignUpRequestDto;
import com.sendback.domain.user.dto.request.UpdateUserInfoRequestDto;
import com.sendback.domain.user.dto.response.UpdateUserInfoResponseDto;
import com.sendback.domain.user.dto.response.UserInfoResponseDto;
import com.sendback.domain.user.service.UserService;
import com.sendback.global.common.ApiResponse;
import com.sendback.global.common.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sendback.domain.auth.dto.Token;
import com.sendback.domain.user.dto.response.CheckUserNicknameResponseDto;
import jakarta.validation.Valid;
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

    @GetMapping("/me")
    public ApiResponse<UserInfoResponseDto> getUserInfo(@UserId Long userId) {
        UserInfoResponseDto responseDto = userService.getUserInfo(userId);
        return ApiResponse.success(responseDto);
    }

    @PutMapping("/me")
    public ApiResponse<UpdateUserInfoResponseDto> updateUserInfo(@UserId Long userId, @RequestBody UpdateUserInfoRequestDto updateUserInfoRequestDto) {
        return ApiResponse.success(userService.updateUserInfo(userId, updateUserInfoRequestDto));
    }
}
