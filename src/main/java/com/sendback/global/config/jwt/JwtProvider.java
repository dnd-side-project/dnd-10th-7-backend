package com.sendback.global.config.jwt;

import com.sendback.domain.auth.dto.Token;
import com.sendback.global.config.redis.RedisService;
import com.sendback.global.exception.type.UnAuthorizedException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import static com.sendback.domain.auth.exception.AuthExceptionType.*;

@Getter
@Component
@RequiredArgsConstructor
public class JwtProvider {
    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.access-token-expire-time}")
    private long ACCESS_TOKEN_EXPIRE_TIME;
    @Value("${jwt.refresh-token-expire-time}")
    private long REFRESH_TOKEN_EXPIRE_TIME;

    private final RedisService redisService;

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION);

        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER)){
            return bearerToken.substring(7);
        }
        return null;
    }

    public Token issueToken(Long userId) {
        Token token = new Token(generateToken(userId, true), generateToken(userId, false));
        redisService.put(userId, token.refreshToken(), REFRESH_TOKEN_EXPIRE_TIME);
        return token;
    }

    private String generateToken(Long userId, boolean isAccessToken) {
        final Date now = new Date();
        final Date expiration = new Date(now.getTime() + (isAccessToken ? ACCESS_TOKEN_EXPIRE_TIME : REFRESH_TOKEN_EXPIRE_TIME));
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setSubject(String.valueOf(userId))
                .claim("type", isAccessToken ? "ACCESS_TOKEN" : "REFRESH_TOKEN")
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateAccessToken(String accessToken) {
        try {
            getJwtParser().parseClaimsJws(accessToken);
            return true;
        } catch (ExpiredJwtException e) {
            throw new UnAuthorizedException(EXPIRED_ACCESS_TOKEN);
        } catch (Exception e) {
            throw new UnAuthorizedException(INVALID_ACCESS_TOKEN_VALUE);
        }
    }

    public boolean validateRefreshToken(String refreshToken) {
        try {
            getJwtParser().parseClaimsJws(refreshToken);
            return true;
        } catch (ExpiredJwtException e) {
            throw new UnAuthorizedException(EXPIRED_REFRESH_TOKEN);
        } catch (Exception e) {
            throw new UnAuthorizedException(INVALID_REFRESH_TOKEN_VALUE);
        }
    }

    public void equalsRefreshToken(Long userId, String refreshToken) {
        if (!redisService.validateRefreshToken(userId, refreshToken)) {
            throw new UnAuthorizedException(NOT_MATCH_REFRESH_TOKEN);
        }
    }

    public Long getSubject(String token) {
        return Long.valueOf(getJwtParser().parseClaimsJws(token)
                .getBody()
                .getSubject());
    }

    private JwtParser getJwtParser() {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build();
    }

    private Key getSigningKey() {
        String encoded = Base64.getEncoder().encodeToString(secretKey.getBytes());
        return Keys.hmacShaKeyFor(encoded.getBytes());
    }

    public Long parseRefreshToken(String token) {
        try {
            Claims claims = getJwtParser().parseClaimsJws(token).getBody();
            return Long.parseLong(claims.getSubject());
        }
        catch (Exception ex) {
            throw new UnAuthorizedException(INVALID_REFRESH_TOKEN_VALUE);
        }
    }
}
