package com.sendback.global.exception.response;

import com.sendback.global.exception.BaseException;
import com.sendback.global.exception.ExceptionType;

public record ExceptionResponse(
        int code,
        String message
) {
    public static ExceptionResponse from(ExceptionType exceptionType) {
        return new ExceptionResponse(exceptionType.getStatusCode(), exceptionType.getMessage());
    }
}
