package com.sendback.global.error.type;

import com.sendback.global.error.ErrorCode;

public class UnAuthorizedException extends SendBackException {
	public UnAuthorizedException() {
		super(ErrorCode.UNAUTHORIZED);
	}

	public UnAuthorizedException(ErrorCode errorCode) {
		super(errorCode);
	}
}