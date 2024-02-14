package com.sendback.domain.like.controller;

import com.sendback.domain.like.dto.response.ReactLikeResponseDto;
import com.sendback.domain.like.service.LikeService;
import com.sendback.global.common.ApiResponse;
import com.sendback.global.common.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.sendback.global.common.ApiResponse.success;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/projects")
public class LikeController {

    private final LikeService likeService;

    @PutMapping("/{projectId}/like")
    public ApiResponse<ReactLikeResponseDto> reactLike(
            @UserId Long userId,
            @PathVariable Long projectId) {
        return success(likeService.reactLike(userId, projectId));
    }

}
