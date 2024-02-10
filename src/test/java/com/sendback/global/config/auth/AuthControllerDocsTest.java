package com.sendback.global.config.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sendback.domain.auth.controller.AuthController;
import com.sendback.domain.auth.dto.Token;
import com.sendback.domain.auth.dto.request.RefreshTokenRequestDto;
import com.sendback.domain.auth.dto.response.TokensResponseDto;
import com.sendback.domain.auth.service.AuthService;
import com.sendback.domain.auth.service.GoogleService;
import com.sendback.domain.auth.service.KakaoService;
import com.sendback.global.config.RestDocsSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.BDDMockito.given;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
public class AuthControllerDocsTest {

//    private final AuthService authService = mock(AuthService.class);
//    private final KakaoService kakaoService = mock(KakaoService.class);
//    private final GoogleService googleService = mock(GoogleService.class);

    private ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    MockMvc mockMvc;
    @MockBean
    KakaoService kakaoService;
    @MockBean
    GoogleService googleService;
    @MockBean
    AuthService authService;

//    @Override
//    protected Object initController() {
//        return new AuthController(kakaoService, googleService, authService);
//    }
    @Test
    @WithMockUser
    @DisplayName("카카오 로그인을 성공하면 access token, refresh token을 반환한다.")
    void loginKakao() throws Exception{

        // given
        String code = "123456";
        String accessToken = "abcdefg";
        String refreshToken = "qwerstu";
        given(kakaoService.loginKakao(code)).willReturn(
                new TokensResponseDto(accessToken, refreshToken)
        );

        // when &then
        mockMvc.perform(
                get("/api/auth/kakao/callback").param("code", code))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("성공"))
                .andExpect(jsonPath("$.data.accessToken").value(accessToken))
                .andExpect(jsonPath("$.data.refreshToken").value(refreshToken))
                .andDo(print());

        verify(kakaoService).loginKakao(code);
    }
    @Test
    @WithMockUser
    @DisplayName("구글 로그인을 성공하면 access token, refresh token을 반환한다.")
    void loginGoogle() throws Exception {

        // given
        String code = "123456";
        String accessToken = "abcdefg";
        String refreshToken = "qwerstu";
        given(googleService.loginGoogle(code)).willReturn(
                new TokensResponseDto(accessToken, refreshToken)
        );

        // when &then
        mockMvc.perform(
                        get("/api/auth/google/callback").param("code", code))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("성공"))
                .andExpect(jsonPath("$.data.accessToken").value(accessToken))
                .andExpect(jsonPath("$.data.refreshToken").value(refreshToken))
                .andDo(print());

        verify(googleService).loginGoogle(code);
    }

    @Test
    @WithMockUser
    @DisplayName("refresh token을 재발급한다")
    void reissueToken() throws Exception{

        // given
        RefreshTokenRequestDto refreshTokenRequestDto = new RefreshTokenRequestDto("qwer");
        given(authService.reissueToken(refreshTokenRequestDto.refreshToken())).willReturn(
                new Token("abcde", "erty")
        );
        String content = objectMapper.writeValueAsString(refreshTokenRequestDto);

        // when &then
        mockMvc.perform(
                    post("/api/auth/reissue")
                            .content(content)
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("성공"))
                .andExpect(jsonPath("$.data.accessToken").value("abcde"))
                .andExpect(jsonPath("$.data.refreshToken").value("erty"))
                .andDo(print());

        verify(authService).reissueToken(refreshTokenRequestDto.refreshToken());
    }

    @Test
    void logoutSocial() {
    }

    @Test
    void tokenReissue() {
    }
}