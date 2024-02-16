package com.sendback.domain.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sendback.domain.auth.dto.SocialUserInfo;
import com.sendback.domain.auth.dto.Token;
import com.sendback.domain.auth.dto.response.SignTokenResponseDto;
import com.sendback.domain.auth.dto.response.TokensResponseDto;
import com.sendback.domain.user.entity.User;
import com.sendback.domain.user.repository.UserRepository;
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
public class GoogleService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    @Value("${oauth2.google.clientId}")
    private String GOOGLE_CLIENT_ID;
    @Value("${oauth2.google.clientSecret}")
    private String GOOGLE_CLIENT_SECRET;
    @Value("${oauth2.google.redirectUri}")
    private String GOOGLE_REDIRECT_URL;
    @Value("${oauth2.google.tokenUri}")
    private String GOOGLE_TOKEN_URI;
    @Value("${oauth2.google.userInfoUri}")
    private String GOOGLE_USERINFO_URI;
    private final RestTemplate rt;

    @Transactional
    public TokensResponseDto loginGoogle(String code) throws JsonProcessingException {
        String accessToken = getAccessToken(code);
        SocialUserInfo googleUserInfo = getGoogleUserInfo(accessToken);

        User googleUser = userRepository.findBySocialId(googleUserInfo.id()).orElse(null);

        if (googleUser == null) {
            String signToken = jwtProvider.generateSignToken(googleUserInfo);
            throw new SignInException(NEED_TO_SIGNUP, new SignTokenResponseDto(signToken));
        }

        Token token =  jwtProvider.issueToken(googleUser.getId());
        return new TokensResponseDto(token.accessToken(), token.refreshToken());
    }

    private String getAccessToken(String code) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", GOOGLE_CLIENT_ID);
        body.add("client_secret", GOOGLE_CLIENT_SECRET);
        body.add("redirect_uri", GOOGLE_REDIRECT_URL);
        body.add("code", code);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> googleTokenRequest =
                new HttpEntity<>(body, headers);

        ResponseEntity<String> response = rt.exchange(
                GOOGLE_TOKEN_URI,
                HttpMethod.POST,
                googleTokenRequest,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    private SocialUserInfo getGoogleUserInfo(String accessToken) throws JsonProcessingException {

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> googleUserInfoRequest = new HttpEntity<>(headers);

        ResponseEntity<String> response = rt.exchange(
                GOOGLE_USERINFO_URI,
                HttpMethod.GET,
                googleUserInfoRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        String id = jsonNode.get("id").asText();
        String nickname = jsonNode.get("name").asText();
        String email = jsonNode.get("email").asText();
        String profile_image = jsonNode.get("picture").asText();

        return new SocialUserInfo(id, nickname, email, profile_image);
    }

}
