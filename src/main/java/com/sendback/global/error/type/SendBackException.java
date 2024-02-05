package com.sendback.global.error.type;

import com.sendback.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class SendBackException extends RuntimeException {
    private final ErrorCode errorCode;

    public SendBackException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public SendBackException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}