package com.sendback.global.error.type;

import com.sendback.global.error.ErrorCode;

public class InvalidValueException extends SendBackException {
    public InvalidValueException() {
        super(ErrorCode.BAD_REQUEST);
    }

    public InvalidValueException(ErrorCode errorCode) {
        super(errorCode);
    }
}