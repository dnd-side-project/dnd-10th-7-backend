package com.sendback.global.error;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {

    private Integer code;
    private String message;

    public ErrorResponse(int status, String message) {
        this.code = status;
        this.message = message;
    }

    public static ErrorResponse of(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .code(errorCode.getHttpStatus().value())
                .message(errorCode.getMessage())
                .build();
    }
}
