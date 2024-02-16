package com.sendback.domain.user.controller;

import com.sendback.domain.auth.dto.Token;
import com.sendback.domain.user.dto.request.SignUpRequestDto;
import com.sendback.domain.user.dto.request.UpdateUserInfoRequestDto;
import com.sendback.domain.user.dto.response.CheckUserNicknameResponseDto;
import com.sendback.domain.user.dto.response.UpdateUserInfoResponseDto;
import com.sendback.domain.user.dto.response.UserInfoResponseDto;
import com.sendback.global.ControllerTest;
import com.sendback.global.WithMockCustomUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;
import java.util.Arrays;
import java.util.List;
import static org.mockito.ArgumentMatchers.*;
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
    class signUp {
        @Test
        @DisplayName("회원 가입을 성공하면 200 상태코드와 함께 access token, refresh token을 반환한다.")
        @WithMockCustomUser
        void signUpKakao_success() throws Exception {
            // given
            String accessToken = "valid accessToken";
            String refreshToken = "valid refreshToken";
            SignUpRequestDto signUpRequestDto = new SignUpRequestDto("user", "2000.01.01",
                    "남자", "백엔드", Arrays.asList("환경", "게임"), "valid signToken");
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

    @Nested
    @DisplayName("내 정보 조회")
    class getUserInfo {
        @Test
        @DisplayName("내 정보와 함께 200 상태코드를 반환한다.")
        @WithMockCustomUser
        void getUserInfo_success() throws Exception {

            // given
            UserInfoResponseDto userInfoResponseDto = new UserInfoResponseDto("test", "backend", "mock_image_url",
                    "2000.01.01", "mock@kakao.com", List.of("game", "health"), 1, 2l, 2l, 2l, 2l);
            given(userService.getUserInfo(anyLong())).willReturn(userInfoResponseDto);

            // when
            ResultActions resultActions = mockMvc.perform(get("/api/users/me")
                            .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX + "AccessToken").with(csrf()))
                    // HTTP 상태코드 200 (OK) 확인
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("200"))
                    .andExpect(jsonPath("$.message").value("성공"))
                    .andExpect(jsonPath("$.data.nickname").value("test"))
                    .andExpect(jsonPath("$.data.career").value("backend"))
                    .andExpect(jsonPath("$.data.profileImageUrl").value("mock_image_url"))
                    .andExpect(jsonPath("$.data.birthday").value("2000.01.01"))
                    .andExpect(jsonPath("$.data.email").value("mock@kakao.com"))
                    .andExpect(jsonPath("$.data.field[0]").value("game"))
                    .andExpect(jsonPath("$.data.field[1]").value("health"))
                    .andExpect(jsonPath("$.data.level").value(1))
                    .andExpect(jsonPath("$.data.feedbackCount").value(2l))
                    .andExpect(jsonPath("$.data.needToFeedbackCount").value(2l))
                    .andExpect(jsonPath("$.data.projectCount").value(2l))
                    .andExpect(jsonPath("$.data.likeCount").value(2l))
                    .andDo(print());

            // then
            resultActions
                    .andDo(document("getUserInfo-success",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            responseFields(
                                    fieldWithPath("code").type(JsonFieldType.NUMBER)
                                            .description("코드"),
                                    fieldWithPath("data").type(JsonFieldType.OBJECT)
                                            .description("응답 데이터"),
                                    fieldWithPath("data.nickname").type(JsonFieldType.STRING)
                                            .description("닉네임"),
                                    fieldWithPath("data.career").type(JsonFieldType.STRING)
                                            .description("직업"),
                                    fieldWithPath("data.profileImageUrl").type(JsonFieldType.STRING)
                                            .description("프로필 이미지"),
                                    fieldWithPath("data.birthday").type(JsonFieldType.STRING)
                                            .description("생일"),
                                    fieldWithPath("data.email").type(JsonFieldType.STRING)
                                            .description("이메일"),
                                    fieldWithPath("data.field").type(JsonFieldType.ARRAY)
                                            .description("관심사"),
                                    fieldWithPath("data.level").type(JsonFieldType.NUMBER)
                                            .description("레벨"),
                                    fieldWithPath("data.feedbackCount").type(JsonFieldType.NUMBER)
                                            .description("피드백 작성 수"),
                                    fieldWithPath("data.needToFeedbackCount").type(JsonFieldType.NUMBER)
                                            .description("다음 레벨까지 필요한 피드백 개수"),
                                    fieldWithPath("data.projectCount").type(JsonFieldType.NUMBER)
                                            .description("프로젝트 등록 수"),
                                    fieldWithPath("data.likeCount").type(JsonFieldType.NUMBER)
                                            .description("좋아요 수"),
                                    fieldWithPath("message").type(JsonFieldType.STRING)
                                            .description("메시지")
                            )));
            verify(userService).getUserInfo(anyLong());
        }
    }

    @Nested
    @DisplayName("내 정보 수정")
    class updateUserInfo {
        @Test
        @DisplayName("내 정보를 수정하면 200 상태코드와 함께 수정된 정보들도 반환한다.")
        @WithMockCustomUser
        void updateUserInfo_success() throws Exception {

            // given
            UpdateUserInfoRequestDto updateUserInfoRequestDto = new UpdateUserInfoRequestDto("테스트 사용자", "200.01.01", "backend",Arrays.asList("환경", "게임"));
            UpdateUserInfoResponseDto updateUserInfoResponseDto = new UpdateUserInfoResponseDto("테스트 사용자", "2000.01.01", "backend",Arrays.asList("환경", "게임"));

            given(userService.updateUserInfo(anyLong(), any(UpdateUserInfoRequestDto.class))).willReturn(updateUserInfoResponseDto);
            String content = objectMapper.writeValueAsString(updateUserInfoRequestDto);

            // when
            ResultActions resultActions = mockMvc.perform(put("/api/users/me")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(content).with(csrf())
                            .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX + "AccessToken"))
                    // HTTP 상태코드 200 (OK) 확인
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("200"))
                    .andExpect(jsonPath("$.message").value("성공"))
                    .andExpect(jsonPath("$.data.nickname").value(updateUserInfoResponseDto.nickname()))
                    .andExpect(jsonPath("$.data.career").value(updateUserInfoResponseDto.career()))
                    .andExpect(jsonPath("$.data.birthday").value(updateUserInfoResponseDto.birthday()))
                    .andExpect(jsonPath("$.data.field[0]").value("환경"))
                    .andExpect(jsonPath("$.data.field[1]").value("게임"))
                    .andDo(print());

            // then
            resultActions
                    .andDo(document("updateUserInfo-success",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            responseFields(
                                    fieldWithPath("code").type(JsonFieldType.NUMBER)
                                            .description("코드"),
                                    fieldWithPath("data").type(JsonFieldType.OBJECT)
                                            .description("응답 데이터"),
                                    fieldWithPath("data.nickname").type(JsonFieldType.STRING)
                                            .description("닉네임"),
                                    fieldWithPath("data.career").type(JsonFieldType.STRING)
                                            .description("직업"),
                                    fieldWithPath("data.birthday").type(JsonFieldType.STRING)
                                            .description("생일"),
                                    fieldWithPath("data.field").type(JsonFieldType.ARRAY)
                                            .description("관심사"),
                                    fieldWithPath("message").type(JsonFieldType.STRING)
                                            .description("메시지")
                            )));
            verify(userService).updateUserInfo(anyLong(), any(UpdateUserInfoRequestDto.class));
        }
    }

}