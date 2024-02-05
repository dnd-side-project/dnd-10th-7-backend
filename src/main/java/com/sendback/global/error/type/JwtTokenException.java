package com.sendback.global.error.type;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class JwtTokenException extends RuntimeException {

    public JwtTokenException(String message) {
        super(message);
    }
}