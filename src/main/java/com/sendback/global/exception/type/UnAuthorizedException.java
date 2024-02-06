package com.sendback.global.exception.type;

import com.sendback.global.exception.BaseException;
import com.sendback.global.exception.ExceptionType;

public class UnAuthorizedException extends BaseException {

	public UnAuthorizedException(ExceptionType exceptionType) {
		super(exceptionType);
	}
}