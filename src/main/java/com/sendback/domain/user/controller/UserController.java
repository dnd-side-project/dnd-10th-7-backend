package com.sendback.domain.user.controller;

import com.sendback.domain.user.dto.request.SignUpRequestDto;
import com.sendback.domain.user.dto.request.UpdateUserInfoRequestDto;
import com.sendback.domain.user.dto.response.*;
import com.sendback.domain.user.service.UserService;
import com.sendback.global.common.ApiResponse;
import com.sendback.global.common.CustomPage;
import com.sendback.global.common.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sendback.domain.auth.dto.Token;
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

    @GetMapping("/me/projects")
    public ApiResponse<CustomPage<RegisteredProjectResponseDto>> getRegisteredProjects(@UserId Long userId, @RequestParam int page,
                                                                                       @RequestParam int size, @RequestParam int sort) {
        return ApiResponse.success(userService.getRegisteredProjects(userId, page, size, sort));
    }

    @GetMapping("/me/scraps")
    public ApiResponse<CustomPage<ScrappedProjectResponseDto>> getScrappedProjects(@UserId Long userId, @RequestParam int page,
                                                                                     @RequestParam int size, @RequestParam int sort) {
        return ApiResponse.success(userService.getScrappedProjects(userId, page, size, sort));
    }

    @GetMapping("/me/feedbacks")
    public ApiResponse<CustomPage<SubmittedFeedbackResponseDto>> getSubmittedProjects(@UserId Long userId, @RequestParam int page,
                                                                                      @RequestParam int size, @RequestParam int sort) {
        return ApiResponse.success(userService.getSubmittedFeedback(userId, page, size, sort));
    }
}
