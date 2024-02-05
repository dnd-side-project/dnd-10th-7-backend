package com.sendback.domain.auth.service;

import com.sendback.domain.auth.dto.Token;
import com.sendback.global.config.redis.RedisService;
import com.sendback.global.config.jwt.JwtProvider;
import com.sendback.global.error.type.UnAuthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtProvider jwtProvider;
    private final RedisService redisService;
    @Value("${jwt.refresh-token-expire-time}")
    private long REFRESH_TOKEN_EXPIRE_TIME;

    @Transactional(noRollbackFor = UnAuthorizedException.class)
    public Token reissueToken(String refreshToken) {
        Long userId = jwtProvider.parseRefreshToken(refreshToken);
        Token tokens = jwtProvider.issueToken(userId);
        redisService.put(userId, tokens.refreshToken(), REFRESH_TOKEN_EXPIRE_TIME);
        return tokens;
    }

    public void logoutSocial(Long userId){
        redisService.delete(userId);
    }
}
