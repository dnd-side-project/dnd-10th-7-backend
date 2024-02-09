package com.sendback.global.config.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sendback.domain.auth.exception.AuthExceptionType;
import com.sendback.global.exception.response.ExceptionResponse;
import com.sendback.global.exception.type.UnAuthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        handleAuthenticationException(response, authException);
    }

    private void handleAuthenticationException(HttpServletResponse response, AuthenticationException authException) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        objectMapper.writeValue(response.getWriter(), ExceptionResponse.from(AuthExceptionType.UNAUTHORIZED));
    }
}