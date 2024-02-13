package com.sendback.global.config.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sendback.domain.auth.exception.AuthExceptionType;
import com.sendback.global.exception.response.ExceptionResponse;
import com.sendback.global.exception.type.UnAuthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    // 유효한 자격증명을 제공하지 않고 접근하려 할때 1000
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        objectMapper.writeValue(response.getWriter(), ExceptionResponse.from(AuthExceptionType.UNAUTHORIZED));
    }
}