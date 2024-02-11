package com.sendback.global.config.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sendback.domain.auth.exception.AuthExceptionType;
import com.sendback.global.exception.response.ExceptionResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    // 필요한 권한이 없이 접근하려 할때 1070
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        objectMapper.writeValue(response.getWriter(), ExceptionResponse.from(AuthExceptionType.ACCESS_DENIED));
    }
}
