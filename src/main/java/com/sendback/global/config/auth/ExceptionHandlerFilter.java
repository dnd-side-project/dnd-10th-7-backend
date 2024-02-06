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
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        response.setStatus(e.getExceptionType().statusCode());
        String body = objectMapper.writeValueAsString(
                ExceptionResponse.from(e.getExceptionType())
        );
        response.getWriter().write(body);
    }

    private void handleException(HttpServletResponse response) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        String body = objectMapper.writeValueAsString(
                ExceptionResponse.from(AuthExceptionType.INTERNAL_SERVER_ERROR)
        );
        response.getWriter().write(body);
    }
}