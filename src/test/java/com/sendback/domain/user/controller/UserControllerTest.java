package com.sendback.domain.user.controller;

import com.sendback.domain.auth.dto.Token;
import com.sendback.domain.user.dto.request.SignUpRequestDto;
import com.sendback.domain.user.dto.response.CheckUserNicknameResponseDto;
import com.sendback.global.ControllerTest;
import com.sendback.global.WithMockCustomUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest extends ControllerTest {

    @Nested
    @DisplayName("회원 가입")
    class saveProject {
        @Test
        @DisplayName("회원 가입을 성공하면 200 상태코드와 함께 access token, refresh token을 반환한다.")
        @WithMockCustomUser
        void signUpKakao_success() throws Exception {
            // given
            String accessToken = "valid accessToken";
            String refreshToken = "valid refreshToken";
            SignUpRequestDto signUpRequestDto = new SignUpRequestDto("user", "2000.01.01",
                    "male", "backend", Arrays.asList("health", "game"), "valid signToken");
            given(userService.signUpUser(signUpRequestDto)).willReturn(new Token(accessToken, refreshToken));

            String content = objectMapper.writeValueAsString(signUpRequestDto);

            // when
            ResultActions resultActions = mockMvc.perform(
                            post("/api/users/signup")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(content).with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("200"))
                    .andExpect(jsonPath("$.message").value("성공"))
                    .andExpect(jsonPath("$.data.accessToken").value(accessToken))
                    .andExpect(jsonPath("$.data.refreshToken").value(refreshToken))
                    .andDo(print());

            // then
            resultActions.andDo(document("signUpKakao-success",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            requestFields(
                                    fieldWithPath("nickname").type(JsonFieldType.STRING)
                                            .description("닉네임"),
                                    fieldWithPath("birthday").type(JsonFieldType.STRING)
                                            .description("닉네임"),
                                    fieldWithPath("gender").type(JsonFieldType.STRING)
                                            .description("닉네임"),
                                    fieldWithPath("career").type(JsonFieldType.STRING)
                                            .description("닉네임"),
                                    fieldWithPath("interests").type(JsonFieldType.ARRAY)
                                            .description("닉네임"),
                                    fieldWithPath("signToken").type(JsonFieldType.STRING)
                                            .description("sign 토큰")
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

            verify(userService).signUpUser(signUpRequestDto);
        }
    }

    @Nested
    @DisplayName("닉네임 중복 검사")
    class checkNickname {
        @Test
        @DisplayName("닉네임 중복 여부를 검사해서 200 상태코드와 ture or false를 반환한다.")
        @WithMockCustomUser
        void checkUserNickname() throws Exception {
            // given\
            String nickname = "test user";
            CheckUserNicknameResponseDto responseDto = new CheckUserNicknameResponseDto(true);
            given(userService.checkUserNickname(nickname)).willReturn(responseDto);

            // when
            ResultActions resultActions = mockMvc.perform(get("/api/users/check")
                            .param("nickname", nickname))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("200"))
                    .andExpect(jsonPath("$.message").value("성공"))
                    .andExpect(jsonPath("$.data.check").value(true))
                    .andDo(print());

            // then
            resultActions
                    .andDo(document("checkUserNickname-success",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            queryParameters(
                                    parameterWithName("nickname").description("닉네임")
                            ),
                            responseFields(
                                    fieldWithPath("code").type(JsonFieldType.NUMBER)
                                            .description("코드"),
                                    fieldWithPath("data").type(JsonFieldType.OBJECT)
                                            .description("응답 데이터"),
                                    fieldWithPath("data.check").type(JsonFieldType.BOOLEAN)
                                            .description("중복 여부"),
                                    fieldWithPath("message").type(JsonFieldType.STRING)
                                            .description("메시지")
                            )));
            verify(userService).checkUserNickname(nickname);
        }
    }

}
