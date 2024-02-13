package com.sendback.domain.field.exception;

import com.sendback.global.exception.ExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FieldExceptionType implements ExceptionType {

    NOT_FOUND_FIELD(5000, "분야의 정보가 올바르지 않습니다.");

    private final int statusCode;
    private final String message;
}
