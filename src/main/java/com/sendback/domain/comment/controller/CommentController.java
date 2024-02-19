package com.sendback.domain.comment.controller;

import com.sendback.domain.comment.dto.request.SaveCommentRequestDto;
import com.sendback.domain.comment.dto.response.SaveCommentResponseDto;
import com.sendback.domain.comment.service.CommentService;
import com.sendback.global.common.ApiResponse;
import com.sendback.global.common.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import static com.sendback.global.common.ApiResponse.success;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/{projectId}/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/")
    public ApiResponse<SaveCommentResponseDto> saveComment(@UserId Long userId, @PathVariable Long projectId,
                                                           @RequestBody SaveCommentRequestDto saveCommentRequestDto) {
        return success(commentService.saveComment(userId, projectId, saveCommentRequestDto));
    }

}
