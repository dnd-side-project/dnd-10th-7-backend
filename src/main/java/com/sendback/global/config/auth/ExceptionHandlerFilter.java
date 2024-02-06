package com.sendback.global.config.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sendback.domain.auth.exception.AuthExceptionType;
import com.sendback.global.exception.response.ExceptionResponse;
import com.sendback.global.exception.type.UnAuthorizedException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (UnAuthorizedException e) {
            handleUnauthorizedException(response, e);
            return; // 예외 처리 후 필터 체인을 더 이상 진행하지 않도록 return 추가
        } catch (Exception ee) {
            handleException(response);
            return; // 예외 처리 후 필터 체인을 더 이상 진행하지 않도록 return 추가
        }
    }

    private void handleUnauthorizedException(HttpServletResponse response, UnAuthorizedException e) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        objectMapper.writeValue(response.getWriter(), ExceptionResponse.from(AuthExceptionType.INVALID_ACCESS_TOKEN_VALUE));
    }

    private void handleException(HttpServletResponse response) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        objectMapper.writeValue(response.getWriter(), ExceptionResponse.from(AuthExceptionType.INTERNAL_SERVER_ERROR));
    }
}