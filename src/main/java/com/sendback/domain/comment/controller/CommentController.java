package com.sendback.domain.comment.controller;

import com.sendback.domain.comment.dto.request.SaveCommentRequestDto;
import com.sendback.domain.comment.dto.response.GetCommentsResponseDto;
import com.sendback.domain.comment.dto.response.SaveCommentResponseDto;
import com.sendback.domain.comment.service.CommentService;
import com.sendback.global.common.ApiResponse;
import com.sendback.global.common.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import static com.sendback.global.common.ApiResponse.success;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/{projectId}/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("")
    public ApiResponse<SaveCommentResponseDto> saveComment(@UserId Long userId, @PathVariable Long projectId,
                                                           @RequestBody SaveCommentRequestDto saveCommentRequestDto) {
        return success(commentService.saveComment(userId, projectId, saveCommentRequestDto));
    }

    @GetMapping("")
    public ApiResponse<List<GetCommentsResponseDto>> getCommentList(@PathVariable Long projectId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() == "anonymousUser") {
            return success(commentService.getCommentList(null , projectId));
        }
        Long userId = (Long) authentication.getPrincipal();
        return success(commentService.getCommentList(userId, projectId));
    }
}
