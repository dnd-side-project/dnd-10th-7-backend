package com.sendback.global.exception.response;

import com.sendback.global.exception.BaseException;
import com.sendback.global.exception.ExceptionType;

public record ExceptionResponse(
        int code,
        String message,

        Object data
) {
    public static ExceptionResponse from(ExceptionType exceptionType) {
        return new ExceptionResponse(exceptionType.getStatusCode(), exceptionType.getMessage(), null);
    }

    public static ExceptionResponse of(int code, String message) {
        return new ExceptionResponse(code, message, null);
    }

    public static ExceptionResponse of(ExceptionType exceptionType, Object data) {
        return new ExceptionResponse(exceptionType.getStatusCode(), exceptionType.getMessage(), data);
    }
}
