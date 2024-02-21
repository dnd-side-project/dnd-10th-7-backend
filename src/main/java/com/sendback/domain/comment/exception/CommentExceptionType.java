package com.sendback.domain.comment.exception;

import com.sendback.global.exception.ExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommentExceptionType implements ExceptionType {

    NOT_FOUND_COMMENT(9000, "댓글을 찾을 수 없습니다."),
    NOT_COMMENT_AUTHOR(9010, "댓글 작성자가 아닙니다.");

    private final int statusCode;
    private final String message;
}
