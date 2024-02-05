package com.sendback.global.exception.type;

import com.sendback.global.exception.BaseException;
import com.sendback.global.exception.ExceptionType;

public class NotFoundException extends BaseException {

    public NotFoundException(ExceptionType exceptionType) {
        super(exceptionType);
    }

}
