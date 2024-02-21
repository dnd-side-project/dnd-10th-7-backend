package com.sendback.domain.comment.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;

public record GetCommentsResponseDto (

        Long userId,
        String nickname,
        String profileImageUrl,
        Long commentId,
        String content,
        @JsonFormat(pattern = "yyyy.MM.dd")
        LocalDateTime createdAt,
        Boolean isAuthor
) {
}
