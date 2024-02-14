package com.sendback.global.exception.type;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.sendback.global.exception.BaseException;
import com.sendback.global.exception.ExceptionType;

public class SignInException extends BaseException {
    public Object data;
    public SignInException(ExceptionType exceptionType) {
        super(exceptionType);
    }

    public SignInException(ExceptionType exceptionType, Object data) {
        super(exceptionType);
        this.data = data;
    }
}
