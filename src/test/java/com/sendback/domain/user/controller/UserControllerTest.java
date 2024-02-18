package com.sendback.domain.user.controller;

import com.sendback.domain.auth.dto.Token;
import com.sendback.domain.user.dto.request.SignUpRequestDto;
import com.sendback.domain.user.dto.request.UpdateUserInfoRequestDto;
import com.sendback.domain.user.dto.response.*;
import com.sendback.global.ControllerTest;
import com.sendback.global.WithMockCustomUser;
import com.sendback.global.common.CustomPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.test.web.servlet.ResultActions;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
                                    .content(content).with(csrf().asHeader()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("200"))
                    .andExpect(jsonPath("$.message").value("성공"))
                    .andExpect(jsonPath("$.data.accessToken").value(accessToken))
                    .andExpect(jsonPath("$.data.refreshToken").value(refreshToken))
                    .andDo(print());

            // then
            resultActions.andDo(document("signUpKakao-success",
                            customRequestPreprocessor(),
                            preprocessResponse(prettyPrint()),
                            requestFields(
                                    fieldWithPath("nickname").type(JsonFieldType.STRING)
                                            .description("닉네임"),
                                    fieldWithPath("birthday").type(JsonFieldType.STRING)
                                            .description("생일"),
                                    fieldWithPath("gender").type(JsonFieldType.STRING)
                                            .description("성별"),
                                    fieldWithPath("career").type(JsonFieldType.STRING)
                                            .description("직업"),
                                    fieldWithPath("fields").type(JsonFieldType.ARRAY)
                                            .description("관심사"),
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
                            customRequestPreprocessor(),
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
            UserInfoResponseDto userInfoResponseDto = new UserInfoResponseDto("test", "백엔드", "mock_image_url",
                    "2000.01.01", "mock@kakao.com", List.of("게임", "환경"), 1, 2l, 2l, 2l, 2l);
            given(userService.getUserInfo(anyLong())).willReturn(userInfoResponseDto);

            // when
            ResultActions resultActions = mockMvc.perform(get("/api/users/me")
                            .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX + "AccessToken").with(csrf().asHeader()))
                    // HTTP 상태코드 200 (OK) 확인
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("200"))
                    .andExpect(jsonPath("$.message").value("성공"))
                    .andExpect(jsonPath("$.data.nickname").value("test"))
                    .andExpect(jsonPath("$.data.career").value("백엔드"))
                    .andExpect(jsonPath("$.data.profileImageUrl").value("mock_image_url"))
                    .andExpect(jsonPath("$.data.birthday").value("2000.01.01"))
                    .andExpect(jsonPath("$.data.email").value("mock@kakao.com"))
                    .andExpect(jsonPath("$.data.fields[0]").value("게임"))
                    .andExpect(jsonPath("$.data.fields[1]").value("환경"))
                    .andExpect(jsonPath("$.data.level").value(1))
                    .andExpect(jsonPath("$.data.feedbackCount").value(2l))
                    .andExpect(jsonPath("$.data.needToFeedbackCount").value(2l))
                    .andExpect(jsonPath("$.data.projectCount").value(2l))
                    .andExpect(jsonPath("$.data.likeCount").value(2l))
                    .andDo(print());

            // then
            resultActions
                    .andDo(document("getUserInfo-success",
                            customRequestPreprocessor(),
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
                                    fieldWithPath("data.fields").type(JsonFieldType.ARRAY)
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
            UpdateUserInfoRequestDto updateUserInfoRequestDto = new UpdateUserInfoRequestDto("테스트 사용자", "2000.01.01", "백엔드",Arrays.asList("환경", "게임"));
            UpdateUserInfoResponseDto updateUserInfoResponseDto = new UpdateUserInfoResponseDto("테스트 사용자", "2000.01.01", "백엔드",Arrays.asList("환경", "게임"));

            given(userService.updateUserInfo(anyLong(), any(UpdateUserInfoRequestDto.class))).willReturn(updateUserInfoResponseDto);
            String content = objectMapper.writeValueAsString(updateUserInfoRequestDto);

            // when
            ResultActions resultActions = mockMvc.perform(put("/api/users/me")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(content).with(csrf().asHeader())
                            .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX + "AccessToken"))
                    // HTTP 상태코드 200 (OK) 확인
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("200"))
                    .andExpect(jsonPath("$.message").value("성공"))
                    .andExpect(jsonPath("$.data.nickname").value(updateUserInfoResponseDto.nickname()))
                    .andExpect(jsonPath("$.data.career").value(updateUserInfoResponseDto.career()))
                    .andExpect(jsonPath("$.data.birthday").value(updateUserInfoResponseDto.birthday()))
                    .andExpect(jsonPath("$.data.fields[0]").value("환경"))
                    .andExpect(jsonPath("$.data.fields[1]").value("게임"))
                    .andDo(print());

            // then
            resultActions
                    .andDo(document("updateUserInfo-success",
                            customRequestPreprocessor(),
                            preprocessResponse(prettyPrint()),
                            requestFields(
                                    fieldWithPath("nickname").type(JsonFieldType.STRING)
                                            .description("닉네임"),
                                    fieldWithPath("career").type(JsonFieldType.STRING)
                                            .description("직업"),
                                    fieldWithPath("birthday").type(JsonFieldType.STRING)
                                            .description("생일"),
                                    fieldWithPath("fields").type(JsonFieldType.ARRAY)
                                            .description("관심사")
                            ),
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
                                    fieldWithPath("data.fields").type(JsonFieldType.ARRAY)
                                            .description("관심사"),
                                    fieldWithPath("message").type(JsonFieldType.STRING)
                                            .description("메시지")
                            )));
            verify(userService).updateUserInfo(anyLong(), any(UpdateUserInfoRequestDto.class));
        }
    }

    @Nested
    @DisplayName("내가 등록한 프로젝트 리스트 조회")
    class getRegisteredProjects {
        @Test
        @DisplayName("200 상태코드와 함께 내가 등록한 프로젝트 정보들을 반환한다.")
        @WithMockCustomUser
        void getRegisteredProjects_success() throws Exception {

            // given
            LocalDateTime mockDateTime = LocalDateTime.now();
            RegisteredProjectResponseDto responseDto = new RegisteredProjectResponseDto(1L, "mock_title", "DEVELOPING", "테스트 테스트",
                    "GAME", mockDateTime, 5L);
            List<RegisteredProjectResponseDto> responseDtos = new ArrayList<>();
            responseDtos.add(responseDto);

            CustomPage<RegisteredProjectResponseDto> customPage = CustomPage.<RegisteredProjectResponseDto>builder()
                    .page(1)
                    .size(5)
                    .totalElements(100L)
                    .totalPages(10)
                    .content(responseDtos)  // Add your mocked content here if needed
                    .build();
            given(userService.getRegisteredProjects(anyLong(), anyInt(), anyInt(), anyInt())).willReturn(customPage);

            // when
            ResultActions resultActions = mockMvc.perform(get("/api/users/me/projects")
                            .with(csrf().asHeader())
                            .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX + "AccessToken")
                            .param("page", "1")
                            .param("size", "5")
                            .param("sort", "0"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.code").value("200"))
                        .andExpect(jsonPath("$.message").value("성공"))
                        .andExpect(jsonPath("$.data.page").value(customPage.getPage()))
                        .andExpect(jsonPath("$.data.size").value(customPage.getSize()))
                        .andExpect(jsonPath("$.data.totalElements").value(customPage.getTotalElements()))
                        .andExpect(jsonPath("$.data.totalPages").value(customPage.getTotalPages()))
                        .andExpect(jsonPath("$.data.content").isArray())
                        .andExpect(jsonPath("$.data.content[0].projectId").value(1))
                        .andExpect(jsonPath("$.data.content[0].title").value("mock_title"))
                        .andExpect(jsonPath("$.data.content[0].progress").value("개발중"))
                        .andExpect(jsonPath("$.data.content[0].summary").value("테스트 테스트"))
                        .andExpect(jsonPath("$.data.content[0].field").value("게임"))
                        .andExpect(jsonPath("$.data.content[0].createdAt").value(mockDateTime.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))))
                        .andExpect(jsonPath("$.data.content[0].pullUpCnt").value(5))
                    .andDo(print());

            // then
            resultActions
                    .andDo(document("getRegisteredProjects-success",
                            customRequestPreprocessor(),
                            preprocessResponse(prettyPrint()),
                            queryParameters(
                                    parameterWithName("page").description("페이지"),
                                    parameterWithName("size").description("사이즈"),
                                    parameterWithName("sort").description("정렬 기준")

                            ),
                            responseFields(
                                    fieldWithPath("code").type(JsonFieldType.NUMBER)
                                            .description("상태 코드"),
                                    fieldWithPath("message").type(JsonFieldType.STRING)
                                            .description("메시지"),
                                    fieldWithPath("data").type(JsonFieldType.OBJECT)
                                            .description("응답 데이터"),
                                    fieldWithPath("data.page").type(JsonFieldType.NUMBER)
                                            .description("페이지 번호"),
                                    fieldWithPath("data.size").type(JsonFieldType.NUMBER)
                                            .description("페이지 크기"),
                                    fieldWithPath("data.totalElements").type(JsonFieldType.NUMBER)
                                            .description("전체 요소 수"),
                                    fieldWithPath("data.totalPages").type(JsonFieldType.NUMBER)
                                            .description("전체 페이지 수"),
                                    fieldWithPath("data.content").type(JsonFieldType.ARRAY)
                                            .description("프로젝트 목록"),
                                    fieldWithPath("data.content[].projectId").type(JsonFieldType.NUMBER)
                                            .description("프로젝트 ID"),
                                    fieldWithPath("data.content[].title").type(JsonFieldType.STRING)
                                            .description("프로젝트 제목"),
                                    fieldWithPath("data.content[].progress").type(JsonFieldType.STRING)
                                            .description("프로젝트 진행 상태"),
                                    fieldWithPath("data.content[].summary").type(JsonFieldType.STRING)
                                            .description("프로젝트 요약"),
                                    fieldWithPath("data.content[].field").type(JsonFieldType.STRING)
                                            .description("프로젝트 분야"),
                                    fieldWithPath("data.content[].createdAt").type(JsonFieldType.STRING)
                                            .attributes(Attributes.key("format").value("yyyy.MM.dd"))
                                            .description("프로젝트 등록일"),
                                    fieldWithPath("data.content[].pullUpCnt").type(JsonFieldType.NUMBER)
                                            .description("Pull Up 수")

                            )));
            verify(userService).getRegisteredProjects(anyLong(), anyInt(), anyInt(), anyInt());

        }
    }

    @Nested
    @DisplayName("내가 스크랩한 프로젝트 리스트 조회")
    class getScrappedProjects {
        @Test
        @DisplayName("200 상태코드와 함께 내가 스크랩한 프로젝트 정보들을 반환한다.")
        @WithMockCustomUser
        void getScrappedProjects_success() throws Exception {

            // given
            LocalDateTime mockDateTime = LocalDateTime.now();
            ScrappedProjectResponseDto responseDto = new ScrappedProjectResponseDto(1L, "mock_title", "DEVELOPING", "테스트 테스트",
                    "GAME", mockDateTime, 5L);
            List<ScrappedProjectResponseDto> responseDtos = new ArrayList<>();
            responseDtos.add(responseDto);

            CustomPage<ScrappedProjectResponseDto> customPage = CustomPage.<ScrappedProjectResponseDto>builder()
                    .page(1)
                    .size(5)
                    .totalElements(100L)
                    .totalPages(10)
                    .content(responseDtos)  // Add your mocked content here if needed
                    .build();
            given(userService.getScrappedProjects(anyLong(), anyInt(), anyInt(), anyInt())).willReturn(customPage);

            // when
            ResultActions resultActions = mockMvc.perform(get("/api/users/me/scraps")
                            .with(csrf().asHeader())
                            .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX + "AccessToken")
                            .param("page", "1")
                            .param("size", "5")
                            .param("sort", "0"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("200"))
                    .andExpect(jsonPath("$.message").value("성공"))
                    .andExpect(jsonPath("$.data.page").value(customPage.getPage()))
                    .andExpect(jsonPath("$.data.size").value(customPage.getSize()))
                    .andExpect(jsonPath("$.data.totalElements").value(customPage.getTotalElements()))
                    .andExpect(jsonPath("$.data.totalPages").value(customPage.getTotalPages()))
                    .andExpect(jsonPath("$.data.content").isArray())
                    .andExpect(jsonPath("$.data.content[0].projectId").value(1))
                    .andExpect(jsonPath("$.data.content[0].title").value("mock_title"))
                    .andExpect(jsonPath("$.data.content[0].progress").value("개발중"))
                    .andExpect(jsonPath("$.data.content[0].summary").value("테스트 테스트"))
                    .andExpect(jsonPath("$.data.content[0].field").value("게임"))
                    .andExpect(jsonPath("$.data.content[0].createdAt").value(mockDateTime.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))))
                    .andExpect(jsonPath("$.data.content[0].pullUpCnt").value(5))
                    .andDo(print());

            // then
            resultActions
                    .andDo(document("getScrappedProjects-success",
                            customRequestPreprocessor(),
                            preprocessResponse(prettyPrint()),
                            queryParameters(
                                    parameterWithName("page").description("페이지"),
                                    parameterWithName("size").description("사이즈"),
                                    parameterWithName("sort").description("정렬 기준")

                            ),
                            responseFields(
                                    fieldWithPath("code").type(JsonFieldType.NUMBER)
                                            .description("상태 코드"),
                                    fieldWithPath("message").type(JsonFieldType.STRING)
                                            .description("메시지"),
                                    fieldWithPath("data").type(JsonFieldType.OBJECT)
                                            .description("응답 데이터"),
                                    fieldWithPath("data.page").type(JsonFieldType.NUMBER)
                                            .description("페이지 번호"),
                                    fieldWithPath("data.size").type(JsonFieldType.NUMBER)
                                            .description("페이지 크기"),
                                    fieldWithPath("data.totalElements").type(JsonFieldType.NUMBER)
                                            .description("전체 요소 수"),
                                    fieldWithPath("data.totalPages").type(JsonFieldType.NUMBER)
                                            .description("전체 페이지 수"),
                                    fieldWithPath("data.content").type(JsonFieldType.ARRAY)
                                            .description("프로젝트 목록"),
                                    fieldWithPath("data.content[].projectId").type(JsonFieldType.NUMBER)
                                            .description("프로젝트 ID"),
                                    fieldWithPath("data.content[].title").type(JsonFieldType.STRING)
                                            .description("프로젝트 제목"),
                                    fieldWithPath("data.content[].progress").type(JsonFieldType.STRING)
                                            .description("프로젝트 진행 상태"),
                                    fieldWithPath("data.content[].summary").type(JsonFieldType.STRING)
                                            .description("프로젝트 요약"),
                                    fieldWithPath("data.content[].field").type(JsonFieldType.STRING)
                                            .description("프로젝트 분야"),
                                    fieldWithPath("data.content[].createdAt").type(JsonFieldType.STRING)
                                            .attributes(Attributes.key("format").value("yyyy.MM.dd"))
                                            .description("프로젝트 등록일"),
                                    fieldWithPath("data.content[].pullUpCnt").type(JsonFieldType.NUMBER)
                                            .description("Pull Up 수")

                            )));
            verify(userService).getScrappedProjects(anyLong(), anyInt(), anyInt(), anyInt());

        }
    }
}