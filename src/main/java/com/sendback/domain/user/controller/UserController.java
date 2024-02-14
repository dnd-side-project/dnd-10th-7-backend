package com.sendback.domain.user.controller;

import com.sendback.domain.user.dto.response.UserInfoResponseDto;
import com.sendback.domain.user.service.UserService;
import com.sendback.global.common.ApiResponse;
import com.sendback.global.common.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ApiResponse<UserInfoResponseDto> getUserInfo(@UserId Long userId) {
        UserInfoResponseDto responseDto = userService.getUserInfo(userId);
        return ApiResponse.success(responseDto);
    }
}
