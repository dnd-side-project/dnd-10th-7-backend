package com.sendback.global.exception.response;

import com.sendback.global.exception.ExceptionType;

public record ExceptionResponse<T>(
        int code,
        String message,
        T data
) {
    public static <T> ExceptionResponse<T> from(ExceptionType exceptionType) {
        return new ExceptionResponse<>(exceptionType.getStatusCode(), exceptionType.getMessage(), null);
    }

    public static <T> ExceptionResponse<T> of(int code, String message) {
        return new ExceptionResponse<>(code, message, null);
    }

    public static <T> ExceptionResponse<T> of(ExceptionType exceptionType, T data) {
        return new ExceptionResponse<>(exceptionType.getStatusCode(), exceptionType.getMessage(), data);
    }

    public static <T> ExceptionResponse<T> of(int code, String message, T data) {
        return new ExceptionResponse<>(code, message, data);
    }
}
