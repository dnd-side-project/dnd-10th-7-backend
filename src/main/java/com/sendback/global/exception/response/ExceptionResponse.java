package com.sendback.global.exception.response;

import com.sendback.global.exception.BaseException;
import com.sendback.global.exception.ExceptionType;

public record ExceptionResponse(
        int statusCode,
        String message
) {
    public static ExceptionResponse from(final BaseException e) {
        final ExceptionType exceptionType = e.getExceptionType();
        return new ExceptionResponse(exceptionType.getStatusCode(), exceptionType.getMessage());
    }
}
