package com.sendback.domain.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sendback.domain.auth.dto.SocialUserInfo;
import com.sendback.domain.auth.dto.Token;
import com.sendback.domain.auth.dto.response.TokensResponseDto;
import com.sendback.domain.user.entity.User;
import com.sendback.domain.user.repository.UserRepository;
import com.sendback.global.config.redis.RedisService;
import com.sendback.global.config.jwt.JwtProvider;
import com.sendback.global.exception.type.SignInException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import static com.sendback.domain.auth.exception.AuthExceptionType.NEED_TO_SIGNUP;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KakaoService {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    @Value("${oauth2.kakao.clientId}")
    private String KAKAO_CLIENT_ID;
    @Value("${oauth2.kakao.redirectUri}")
    private String KAKAO_REDIRECT_URI;
    @Value("${oauth2.kakao.userInfoUri}")
    private String KAKAO_USERINFO_URI;
    @Value("${oauth2.kakao.tokenUri}")
    private String KAKAO_TOKEN_URI;
    private final RedisService redisService;
    private final RestTemplate rt;

    @Transactional
    public TokensResponseDto loginKakao(String code) throws JsonProcessingException {
        String accessToken = getAccessToken(code);
        SocialUserInfo kakaoUserInfo = getKakaoUserInfo(accessToken);

        User kakaoUser = userRepository.findBySocialId(kakaoUserInfo.id()).orElse(null);

        if (kakaoUser == null) {
            String signToken = jwtProvider.generateSignToken(kakaoUserInfo.email());
            throw new SignInException(NEED_TO_SIGNUP, signToken);
        }

        Token token =  jwtProvider.issueToken(kakaoUser.getId());
        return new TokensResponseDto(token.accessToken(), token.refreshToken());
    }

    private String getAccessToken(String code) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", KAKAO_CLIENT_ID);
        body.add("redirect_uri", KAKAO_REDIRECT_URI);
        body.add("code", code);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(body, headers);

        ResponseEntity<String> response = rt.exchange(
                KAKAO_TOKEN_URI,
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }
    private SocialUserInfo getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);

        ResponseEntity<String> response = rt.exchange(
                KAKAO_USERINFO_URI,
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        String id = jsonNode.get("id").asText();
        String nickname = jsonNode.get("properties")
                .get("nickname").asText();
        String email = jsonNode.get("kakao_account").get("email").asText();
        String profile_image = jsonNode.get("properties")
                .get("profile_image").asText();

        return new SocialUserInfo(id, nickname, email, profile_image);
    }

}