package com.sendback.domain.comment.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record SaveCommentResponseDto (
    Long commentId,
    String content,
    Long userId,

    String profileImageUrl,
    String nickname,
    @JsonFormat(pattern = "yyyy.MM.dd")
    LocalDateTime createdAt

){
}