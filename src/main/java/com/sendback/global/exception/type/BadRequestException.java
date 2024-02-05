package com.sendback.global.exception.type;

import com.sendback.global.exception.BaseException;
import com.sendback.global.exception.ExceptionType;

public class BadRequestException extends BaseException {

    public BadRequestException(ExceptionType exceptionType) {
        super(exceptionType);
    }

}
