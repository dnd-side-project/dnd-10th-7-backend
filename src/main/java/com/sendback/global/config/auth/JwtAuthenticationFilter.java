package com.sendback.global.config.auth;

import com.sendback.domain.auth.exception.AuthExceptionType;
import com.sendback.global.config.jwt.JwtProvider;
import com.sendback.global.exception.ExceptionType;
import com.sendback.global.exception.type.UnAuthorizedException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.sendback.global.config.auth.SecurityConfig.*;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";
    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(!isPermittedUrl(request)){
            final String accessToken = getAccessTokenFromHttpServletRequest(request);
            jwtProvider.validateAccessToken(accessToken);
            final Long userId = jwtProvider.getSubject(accessToken);
            setAuthentication(request, userId);
        }
        filterChain.doFilter(request, response);
    }

    private String getAccessTokenFromHttpServletRequest(HttpServletRequest request) {
        String accessToken = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(accessToken) && accessToken.startsWith(BEARER)) {
            return accessToken.substring(BEARER.length());
        }
        throw new UnAuthorizedException(AuthExceptionType.INVALID_ACCESS_TOKEN_VALUE);
    }

    private void setAuthentication(HttpServletRequest request, Long userId) {
        UserAuthentication authentication = new UserAuthentication(userId, null, null);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private boolean isPermittedUrl(HttpServletRequest request) {
        for (String urlPattern : PERMITTED_URLS) {
            AntPathRequestMatcher matcher = new AntPathRequestMatcher(urlPattern);
            if (matcher.matches(request)) {
                return true;
            }
        }
        return false;
    }
}



