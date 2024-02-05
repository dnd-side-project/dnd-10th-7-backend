package com.sendback.global.exception.type;

import com.sendback.global.exception.BaseException;
import com.sendback.global.exception.ExceptionType;

public class ForbiddenException extends BaseException {

    public ForbiddenException(ExceptionType exceptionType) {
        super(exceptionType);
    }

}
