package com.sendback.domain.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sendback.domain.auth.controller.AuthController;
import com.sendback.domain.auth.dto.Token;
import com.sendback.domain.auth.dto.request.RefreshTokenRequestDto;
import com.sendback.domain.auth.dto.response.TokensResponseDto;
import com.sendback.domain.auth.service.AuthService;
import com.sendback.domain.auth.service.GoogleService;
import com.sendback.domain.auth.service.KakaoService;
import com.sendback.global.WithMockCustomUser;
import com.sendback.global.config.RestDocsSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.BDDMockito.given;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
public class AuthControllerDocsTest extends RestDocsSupport {

    @MockBean
    KakaoService kakaoService;
    @MockBean
    GoogleService googleService;
    @MockBean
    AuthService authService;

    static final String ACCESS_TOKEN_PREFIX = "Bearer ";

    @Override
    protected Object initController(){
        return new AuthController(kakaoService, googleService, authService);
    }

    @Test
    @DisplayName("카카오 로그인을 성공하면 200 상태코드와 함께 access token, refresh token을 반환한다.")
    @WithMockCustomUser
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
                .andDo(document("login-kakao",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("code").description("인가 코드")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.accessToken").type(JsonFieldType.STRING)
                                        .description("access 토큰"),
                                fieldWithPath("data.refreshToken").type(JsonFieldType.STRING)
                                        .description("refresh 토큰"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메시지")
                        )));

        verify(kakaoService).loginKakao(code);
    }
    @Test
    @DisplayName("구글 로그인을 성공하면 200 상태코드와 함께 access token, refresh token을 반환한다.")
    @WithMockCustomUser
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
                .andDo(print())
                .andDo(document("login-google",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        queryParameters(
                                parameterWithName("code").description("인가 코드")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.accessToken").type(JsonFieldType.STRING)
                                        .description("access 토큰"),
                                fieldWithPath("data.refreshToken").type(JsonFieldType.STRING)
                                        .description("refresh 토큰"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메시지")
                        )));


        verify(googleService).loginGoogle(code);
    }

    @Test
    @DisplayName("refresh token을 정상적으로 재발급하면 200 상태코드를 반환한다.")
    @WithMockCustomUser
    void reissueToken() throws Exception{

        // given
        String accessToken = "abcdefg";
        String refreshToken = "qwerstu";
        RefreshTokenRequestDto refreshTokenRequestDto = new RefreshTokenRequestDto("qwer");
        given(authService.reissueToken(refreshTokenRequestDto.refreshToken())).willReturn(
                new Token(accessToken, refreshToken)
        );
        String content = objectMapper.writeValueAsString(refreshTokenRequestDto);

        // when &then
        mockMvc.perform(
                    post("/api/auth/reissue")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("성공"))
                .andExpect(jsonPath("$.data.accessToken").value(accessToken))
                .andExpect(jsonPath("$.data.refreshToken").value(refreshToken))
                .andDo(print())
                .andDo(document("reissue-token",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("refreshToken").type(JsonFieldType.STRING)
                                    .description("refresh 토큰")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                        .description("응답 데이터"),
                                fieldWithPath("data.accessToken").type(JsonFieldType.STRING)
                                        .description("access 토큰"),
                                fieldWithPath("data.refreshToken").type(JsonFieldType.STRING)
                                        .description("refresh 토큰"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메시지")
                        )));

        verify(authService).reissueToken(refreshTokenRequestDto.refreshToken());
    }

    @Test
    @DisplayName("로그아웃을 성공하면 200 상태코드를 반환한다.")
    @WithMockCustomUser
    void logoutSocial() throws Exception {

        // When & Then
        mockMvc.perform(post("/api/auth/logout")
                        // 인증정보 설정
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX+"AccessToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("성공"))
                .andDo(print())
                .andDo(document("logout-social",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.NUMBER)
                                        .description("코드"),
                                fieldWithPath("data").type(JsonFieldType.NULL)
                                        .description("응답 데이터"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메시지")
                        )));
    }
}