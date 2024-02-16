package com.sendback.domain.auth.controller;

import com.sendback.domain.auth.dto.Token;
import com.sendback.domain.auth.dto.request.RefreshTokenRequestDto;
import com.sendback.domain.auth.dto.response.SignTokenResponseDto;
import com.sendback.domain.auth.dto.response.TokensResponseDto;
import com.sendback.global.ControllerTest;
import com.sendback.global.WithMockCustomUser;
import com.sendback.global.exception.type.SignInException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static com.sendback.domain.auth.exception.AuthExceptionType.NEED_TO_SIGNUP;
import static org.mockito.BDDMockito.given;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerTest extends ControllerTest {


    @Nested
    @DisplayName("카카오 로그인")
    class loginKakao {

        @Test
        @DisplayName("카카오 로그인을 성공하면(기존 회원) 200 상태코드와 함께 access token, refresh token을 반환한다.")
        @WithMockCustomUser
        void loginKakao_success() throws Exception {

            // given
            String code = "valid code";
            String accessToken = "valid accessToken";
            String refreshToken = "valid refreshToken";
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
                    .andDo(document("login-kakao-success",
                            customRequestPreprocessor(),
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
        @DisplayName("회원 가입이 필요하면 1070 상태코드와 함께 sign token을 반환한다.")
        @WithMockCustomUser
        void loginKakao_fail1() throws Exception {

            // given
            String code = "valid code";
            given(kakaoService.loginKakao(code))
                    .willThrow(new SignInException(NEED_TO_SIGNUP, new SignTokenResponseDto("test_sign_token")));

            // when &then
            mockMvc.perform(
                            get("/api/auth/kakao/callback").param("code", code))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("1080"))
                    .andExpect(jsonPath("$.message").value("추가 정보를 입력하세요."))
                    .andExpect(jsonPath("$.data.signToken").value("test_sign_token"))
                    .andDo(document("login-kakao-failure",
                            customRequestPreprocessor(),
                            preprocessResponse(prettyPrint()),
                            queryParameters(
                                    parameterWithName("code").description("인가 코드")
                            ),
                            responseFields(
                                    fieldWithPath("code").type(JsonFieldType.NUMBER)
                                            .description("코드"),
                                    fieldWithPath("data").type(JsonFieldType.OBJECT)
                                            .description("응답 데이터"),
                                    fieldWithPath("data.signToken").type(JsonFieldType.STRING)
                                            .description("sign 토큰"),
                                    fieldWithPath("message").type(JsonFieldType.STRING)
                                            .description("메시지")
                            )));

            verify(kakaoService).loginKakao(code);
        }
    }

    @Nested
    @DisplayName("구글 로그인")
    class loginGoogle {

        @Test
        @DisplayName("구글 로그인을 성공하면 200 상태코드와 함께 access token, refresh token을 반환한다.")
        @WithMockCustomUser
        void loginGoogle_success() throws Exception {

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
                    .andDo(document("login-google-success",
                            customRequestPreprocessor(),
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
        @DisplayName("회원 가입이 필요하면 1070 상태코드와 함께 sign token을 반환한다.")
        @WithMockCustomUser
        void loginGoogle_fail1() throws Exception {

            // given
            String code = "valid code";
            given(googleService.loginGoogle(code))
                    .willThrow(new SignInException(NEED_TO_SIGNUP, new SignTokenResponseDto("test_sign_token")));

            // when
            ResultActions resultActions = mockMvc.perform(
                            get("/api/auth/google/callback").param("code", code))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("1080"))
                    .andExpect(jsonPath("$.message").value("추가 정보를 입력하세요."))
                    .andExpect(jsonPath("$.data.signToken").value("test_sign_token"));

            // then
            resultActions.andDo(document("login-google-failure",
                            customRequestPreprocessor(),
                            preprocessResponse(prettyPrint()),
                            queryParameters(
                                    parameterWithName("code").description("인가 코드")
                            ),
                            responseFields(
                                    fieldWithPath("code").type(JsonFieldType.NUMBER)
                                            .description("코드"),
                                    fieldWithPath("data").type(JsonFieldType.OBJECT)
                                            .description("응답 데이터"),
                                    fieldWithPath("data.signToken").type(JsonFieldType.STRING)
                                            .description("sign 토큰"),
                                    fieldWithPath("message").type(JsonFieldType.STRING)
                                            .description("메시지")
                            )));

            verify(googleService).loginGoogle(code);
        }
    }

    @Nested
    @DisplayName("토큰 재발급")
    class reissueToken {

        @Test
        @DisplayName("refresh token을 정상적으로 재발급하면 200 상태코드를 반환한다.")
        @WithMockCustomUser
        void reissueToken_success() throws Exception {

            // given
            String accessToken = "abcdefg";
            String refreshToken = "qwerstu";
            RefreshTokenRequestDto refreshTokenRequestDto = new RefreshTokenRequestDto("qwer");
            given(authService.reissueToken(refreshTokenRequestDto.refreshToken())).willReturn(
                    new Token(accessToken, refreshToken)
            );
            String content = objectMapper.writeValueAsString(refreshTokenRequestDto);

            // when
            ResultActions resultActions = mockMvc.perform(
                            post("/api/auth/reissue")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(content).with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("200"))
                    .andExpect(jsonPath("$.message").value("성공"))
                    .andExpect(jsonPath("$.data.accessToken").value(accessToken))
                    .andExpect(jsonPath("$.data.refreshToken").value(refreshToken))
                    .andDo(print());

            // then
            resultActions.andDo(document("reissue-token",
                            customRequestPreprocessor(),
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
    }

    @Nested
    @DisplayName("로그아웃")
    class logoutSocial {
        @Test
        @DisplayName("로그아웃을 성공하면 200 상태코드를 반환한다.")
        @WithMockCustomUser
        void logoutSocial_success() throws Exception {

            // when
            ResultActions resultActions = mockMvc.perform(post("/api/auth/logout")
                            // 인증정보 설정
                            .contentType(MediaType.APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX + "AccessToken").with(csrf().asHeader()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("200"))
                    .andExpect(jsonPath("$.message").value("성공"))
                    .andDo(print());

            // then
            resultActions
                    .andDo(document("logout-social",
                            customRequestPreprocessor(),
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
}