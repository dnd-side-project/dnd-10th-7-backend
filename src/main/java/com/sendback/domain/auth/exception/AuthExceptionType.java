package com.sendback.domain.auth.exception;

import com.sendback.global.exception.ExceptionType;
import org.springframework.http.HttpStatus;

public enum AuthExceptionType implements ExceptionType {

    UNAUTHORIZED(1000, "리소스 접근 권한이 없습니다."),
    INTERNAL_SERVER_ERROR(1010, "내부 서버 에러입니다."),
    EXPIRED_ACCESS_TOKEN(1020, "만료된 access token 입니다."),
    INVALID_ACCESS_TOKEN_VALUE(1030, "유효하지 않은 access token 입니다."),
    EXPIRED_REFRESH_TOKEN(1040, "만료된 refresh token 입니다."),
    INVALID_REFRESH_TOKEN_VALUE(1050, "유효하지 않은 refresh token 입니다."),

    NOT_MATCH_REFRESH_TOKEN(1060, "refresh token이 올바르지 않습니다.");

    private final int statusCode;
    private final String message;

    AuthExceptionType(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    @Override
    public int statusCode() {
        return statusCode;
    }

    @Override
    public String message() {
        return message;
    }
}