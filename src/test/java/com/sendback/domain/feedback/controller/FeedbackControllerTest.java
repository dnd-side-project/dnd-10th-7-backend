package com.sendback.domain.feedback.controller;

import com.sendback.domain.feedback.dto.request.SaveFeedbackRequestDto;
import com.sendback.domain.feedback.dto.response.FeedbackDetailResponseDto;
import com.sendback.domain.feedback.dto.response.FeedbackIdResponseDto;
import com.sendback.domain.feedback.dto.response.GetFeedbacksResponse;
import com.sendback.domain.feedback.dto.response.SubmitFeedbackResponseDto;
import com.sendback.global.ControllerTest;
import com.sendback.global.WithMockCustomUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import static com.sendback.domain.feedback.fixture.FeedbackFixture.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FeedbackControllerTest extends ControllerTest {

    @Nested
    @DisplayName("피드백 등록 시")
    class saveFeedback {

        SaveFeedbackRequestDto saveFeedbackRequestDto = MOCK_SAVE_FEEDBACK_REQUEST;

        @Test
        @WithMockCustomUser
        @DisplayName("정상적인 요청이라면 성공을 반환한다.")
        public void success() throws Exception {
            //given
            FeedbackIdResponseDto response = new FeedbackIdResponseDto(1L);
            given(feedbackService.saveFeedback(anyLong(), anyLong(), any())).willReturn(response);

            //when
            ResultActions resultActions = mockMvc.perform(post("/api/projects/{projectId}/feedbacks", 1L)
                            .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX + "AccessToken")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(saveFeedbackRequestDto)).with(csrf().asHeader()))
                    .andDo(print());

            //then
            resultActions
                    .andDo(document("feedback/save",
                            customRequestPreprocessor(),
                            preprocessResponse(prettyPrint()),
                            pathParameters(
                                    parameterWithName("projectId").description("프로젝트 ID")
                            ),
                            requestHeaders(
                                    headerWithName("Authorization").description("JWT 엑세스 토큰")
                            ),
                            requestFields(
                                    fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                                    fieldWithPath("linkUrl").type(JsonFieldType.STRING).description("링크 주소"),
                                    fieldWithPath("content").type(JsonFieldType.STRING).description("내용"),
                                    fieldWithPath("rewardMessage").type(JsonFieldType.STRING).description("추가 리워드 문구"),
                                    fieldWithPath("startedAt").type(JsonFieldType.STRING).description("시작 날짜"),
                                    fieldWithPath("endedAt").type(JsonFieldType.STRING).description("끝나는 날짜")
                            ),
                            responseFields(
                                    fieldWithPath("code").type(JsonFieldType.NUMBER)
                                            .description("코드"),
                                    fieldWithPath("data").type(JsonFieldType.OBJECT)
                                            .description("응답 데이터"),
                                    fieldWithPath("data.feedbackId").type(JsonFieldType.NUMBER)
                                            .description("피드백 ID"),
                                    fieldWithPath("message").type(JsonFieldType.STRING)
                                            .description("메시지")
                            )))
                    .andExpect(jsonPath("$.code").value("200"))
                    .andExpect(jsonPath("$.message").value("성공"))
                    .andExpect(jsonPath("$.data.feedbackId").value(1))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("피드백 상세 조회 시")
    class getFeedbackDetail {

        FeedbackDetailResponseDto mockFeedbackDetailResponseDto = MOCK_FEEDBACK_DETAIL_RESPONSE;

        @Test
        @WithMockCustomUser
        @DisplayName("정상적인 접근 시 성공을 반환한다.")
        public void success() throws Exception {
            //given
            Long projectId = 1L;
            Long feedbackId = 1L;
            given(feedbackService.getFeedbackDetail(anyLong(), anyLong())).willReturn(mockFeedbackDetailResponseDto);

            //when
            ResultActions resultActions = mockMvc.perform(get("/api/projects/{projectId}/feedbacks/{feedbackId}", projectId, feedbackId)
                            .with(csrf().asHeader())).andDo(print());

            //then
            resultActions
                    .andDo(document("feedback/detail",
                            customRequestPreprocessor(),
                            preprocessResponse(prettyPrint()),
                            pathParameters(
                                    parameterWithName("projectId").description("프로젝트 ID"),
                                    parameterWithName("feedbackId").description("피드백 ID")
                            ),
                            responseFields(
                                    fieldWithPath("code").type(JsonFieldType.NUMBER)
                                            .description("코드"),
                                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                                    fieldWithPath("data.userId").type(JsonFieldType.NUMBER).description("유저 ID"),
                                    fieldWithPath("data.username").type(JsonFieldType.STRING).description("유저 이름"),
                                    fieldWithPath("data.userLevel").type(JsonFieldType.STRING).description("유저 레벨"),
                                    fieldWithPath("data.profileImageUrl").type(JsonFieldType.STRING).description("프로필 이미지 주소"),
                                    fieldWithPath("data.feedbackId").type(JsonFieldType.NUMBER).description("피드백 ID"),
                                    fieldWithPath("data.feedbackTitle").type(JsonFieldType.STRING).description("피드백 제목"),
                                    fieldWithPath("data.linkUrl").type(JsonFieldType.STRING).description("피드백 링크"),
                                    fieldWithPath("data.content").type(JsonFieldType.STRING).description("피드백 내용"),
                                    fieldWithPath("data.rewardMessage").type(JsonFieldType.STRING).description("피드백 추가 리워드"),
                                    fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("생성 날짜(날짜,시, 분까지)"),
                                    fieldWithPath("data.startedAt").type(JsonFieldType.STRING).description("시작 날짜"),
                                    fieldWithPath("data.endedAt").type(JsonFieldType.STRING).description("끝나는 날짜"),
                                    fieldWithPath("data.projectId").type(JsonFieldType.NUMBER).description("프로젝트 ID"),
                                    fieldWithPath("data.projectTitle").type(JsonFieldType.STRING).description("프로젝트 제목"),
                                    fieldWithPath("data.fieldName").type(JsonFieldType.STRING).description("분야"),
                                    fieldWithPath("data.progress").type(JsonFieldType.STRING).description("진행 정도"),
                                    fieldWithPath("message").type(JsonFieldType.STRING)
                                            .description("메시지")
                            )))
                    .andExpect(jsonPath("$.code").value("200"))
                    .andExpect(jsonPath("$.message").value("성공"))
                    .andExpect(jsonPath("$.data.username").value(mockFeedbackDetailResponseDto.username()))
                    .andExpect(jsonPath("$.data.userLevel").value(mockFeedbackDetailResponseDto.userLevel()))
                    .andExpect(jsonPath("$.data.profileImageUrl").value(mockFeedbackDetailResponseDto.profileImageUrl()))
                    .andExpect(jsonPath("$.data.feedbackId").value(mockFeedbackDetailResponseDto.feedbackId()))
                    .andExpect(jsonPath("$.data.feedbackTitle").value(mockFeedbackDetailResponseDto.feedbackTitle()))
                    .andExpect(jsonPath("$.data.linkUrl").value(mockFeedbackDetailResponseDto.linkUrl()))
                    .andExpect(jsonPath("$.data.content").value(mockFeedbackDetailResponseDto.content()))
                    .andExpect(jsonPath("$.data.rewardMessage").value(mockFeedbackDetailResponseDto.rewardMessage()))
                    .andExpect(jsonPath("$.data.createdAt").value(mockFeedbackDetailResponseDto.createdAt()))
                    .andExpect(jsonPath("$.data.startedAt").value(mockFeedbackDetailResponseDto.startedAt()))
                    .andExpect(jsonPath("$.data.endedAt").value(mockFeedbackDetailResponseDto.endedAt()))
                    .andExpect(jsonPath("$.data.projectId").value(mockFeedbackDetailResponseDto.projectId()))
                    .andExpect(jsonPath("$.data.projectTitle").value(mockFeedbackDetailResponseDto.projectTitle()))
                    .andExpect(jsonPath("$.data.fieldName").value(mockFeedbackDetailResponseDto.fieldName()))
                    .andExpect(jsonPath("$.data.progress").value(mockFeedbackDetailResponseDto.progress()))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("피드백을 제출 시")
    class submitFeedback {

        SubmitFeedbackResponseDto submitFeedbackResponseDto = MOCK_SUBMIT_FEEDBACK_RESPONSE;
        @Test
        @WithMockCustomUser
        @DisplayName("정상적인 요청이면 성공을 반환한다.")
        public void success() throws Exception {
            //given
            Long projectId = 1L;
            Long feedbackId = 1L;

            MockMultipartFile mockMultipartFile = new MockMultipartFile(
                    "image",
                    "피드백 스크린 샷",
                    MediaType.IMAGE_JPEG_VALUE,
                    "mock image".getBytes()
            );

            given(feedbackService.submitFeedback(anyLong(), anyLong(), any())).willReturn(submitFeedbackResponseDto);


            //when
            ResultActions resultActions = mockMvc.perform(multipart("/api/projects/{projectId}/feedbacks/{feedbackId}", projectId, feedbackId)
                    .file(mockMultipartFile)
                    .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX + "AccessToken")
                    .contentType(MediaType.MULTIPART_FORM_DATA).with(csrf().asHeader()));

            //then
            resultActions
                    .andDo(print())
                    .andDo(document("feedback/submit",
                            customRequestPreprocessor(),
                            preprocessResponse(prettyPrint()),
                            pathParameters(
                                    parameterWithName("projectId").description("프로젝트 ID"),
                                    parameterWithName("feedbackId").description("피드백 ID")
                            ),
                            requestHeaders(
                                    headerWithName("Authorization").description("JWT 엑세스 토큰")
                            ),
                            responseFields(
                                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("코드"),
                                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                                    fieldWithPath("data.level").type(JsonFieldType.STRING).description("유저 레벨"),
                                    fieldWithPath("data.isLevelUp").type(JsonFieldType.BOOLEAN).description("레벨업 했는지 여부"),
                                    fieldWithPath("data.remainFeedbackCount").type(JsonFieldType.NUMBER).description("레벨업까지 남은 피드백 횟수"),
                                    fieldWithPath("message").type(JsonFieldType.STRING)
                                            .description("메시지")
                            )))
                    .andExpect(jsonPath("$.code").value("200"))
                    .andExpect(jsonPath("$.message").value("성공"))
                    .andExpect(jsonPath("$.data.level").value(submitFeedbackResponseDto.level()))
                    .andExpect(jsonPath("$.data.isLevelUp").value(submitFeedbackResponseDto.isLevelUp()))
                    .andExpect(jsonPath("$.data.remainFeedbackCount").value(submitFeedbackResponseDto.remainFeedbackCount()))
                    .andExpect(status().isOk());


        }
    }

    @Nested
    @DisplayName("프로젝트에 따라 최신 순으로 피드백 3개 이하를 조회할 때")
    class getFeedbacks {

        GetFeedbacksResponse getFeedbacksResponse = MOCK_GET_FEEDBACK_RESPONSE;

        @Test
        @DisplayName("정상적인 접근 시 값을 반환한다.")
        @WithMockCustomUser
        public void success() throws Exception {
            //given
            Long projectId = 1L;
            given(feedbackService.getFeedbacks(any(), anyLong())).willReturn(getFeedbacksResponse);

            //when
            ResultActions resultActions = mockMvc.perform(get("/api/projects/{projectId}/feedbacks", projectId)
                    .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX + "AccessToken")
                    .with(csrf().asHeader()))
                    .andDo(print());

            //then
            resultActions
                    .andDo(document("feedback/list",
                            customRequestPreprocessor(),
                            preprocessResponse(prettyPrint()),
                            pathParameters(
                                    parameterWithName("projectId").description("프로젝트 ID")
                            ),
                            requestHeaders(
                                    headerWithName("Authorization").description("JWT 엑세스 토큰").optional()
                            ),
                            responseFields(
                                    fieldWithPath("code").type(JsonFieldType.NUMBER)
                                            .description("코드"),
                                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("데이터"),
                                    fieldWithPath("data.feedbacks").type(JsonFieldType.ARRAY).description("피드백 리스트"),
                                    fieldWithPath("data.feedbacks[].feedbackId").type(JsonFieldType.NUMBER).description("피드백 ID"),
                                    fieldWithPath("data.feedbacks[].title").type(JsonFieldType.STRING).description("제목"),
                                    fieldWithPath("data.feedbacks[].rewardMessage").type(JsonFieldType.STRING).description("추가 리워드"),
                                    fieldWithPath("data.feedbacks[].startedAt").type(JsonFieldType.STRING).description("시작 날짜"),
                                    fieldWithPath("data.feedbacks[].endedAt").type(JsonFieldType.STRING).description("끝나는 날짜"),
                                    fieldWithPath("data.feedbacks[].isFinished").type(JsonFieldType.BOOLEAN).description("피드백 종료 여부"),
                                    fieldWithPath("data.feedbacks[].isAuthor").type(JsonFieldType.BOOLEAN).description("작성자 여부"),
                                    fieldWithPath("data.feedbacks[].isSubmitted").type(JsonFieldType.BOOLEAN).description("제출 여부"),
                                    fieldWithPath("message").type(JsonFieldType.STRING)
                                            .description("메시지")
                            )))
                    .andExpect(jsonPath("$.code").value("200"))
                    .andExpect(jsonPath("$.message").value("성공"))
                    .andExpect(jsonPath("$.data.feedbacks").isArray())
                    .andExpect(jsonPath("$.data.feedbacks[0].feedbackId").value(MOCK_FEEDBACK_RESPONSE_A.feedbackId()))
                    .andExpect(jsonPath("$.data.feedbacks[0].title").value(MOCK_FEEDBACK_RESPONSE_A.title()))
                    .andExpect(jsonPath("$.data.feedbacks[0].rewardMessage").value(MOCK_FEEDBACK_RESPONSE_A.rewardMessage()))
                    .andExpect(jsonPath("$.data.feedbacks[0].startedAt").value(MOCK_FEEDBACK_RESPONSE_A.startedAt()))
                    .andExpect(jsonPath("$.data.feedbacks[0].endedAt").value(MOCK_FEEDBACK_RESPONSE_A.endedAt()))
                    .andExpect(jsonPath("$.data.feedbacks[0].isFinished").value(MOCK_FEEDBACK_RESPONSE_A.isFinished()))
                    .andExpect(jsonPath("$.data.feedbacks[0].isAuthor").value(MOCK_FEEDBACK_RESPONSE_A.isAuthor()))
                    .andExpect(jsonPath("$.data.feedbacks[0].isSubmitted").value(MOCK_FEEDBACK_RESPONSE_A.isSubmitted()))
                    .andExpect(jsonPath("$.data.feedbacks[1].feedbackId").value(MOCK_FEEDBACK_RESPONSE_B.feedbackId()))
                    .andExpect(jsonPath("$.data.feedbacks[1].title").value(MOCK_FEEDBACK_RESPONSE_B.title()))
                    .andExpect(jsonPath("$.data.feedbacks[1].rewardMessage").value(MOCK_FEEDBACK_RESPONSE_B.rewardMessage()))
                    .andExpect(jsonPath("$.data.feedbacks[1].startedAt").value(MOCK_FEEDBACK_RESPONSE_B.startedAt()))
                    .andExpect(jsonPath("$.data.feedbacks[1].endedAt").value(MOCK_FEEDBACK_RESPONSE_B.endedAt()))
                    .andExpect(jsonPath("$.data.feedbacks[1].isFinished").value(MOCK_FEEDBACK_RESPONSE_B.isFinished()))
                    .andExpect(jsonPath("$.data.feedbacks[1].isAuthor").value(MOCK_FEEDBACK_RESPONSE_B.isAuthor()))
                    .andExpect(jsonPath("$.data.feedbacks[1].isSubmitted").value(MOCK_FEEDBACK_RESPONSE_B.isSubmitted()))
                    .andExpect(status().isOk());
        }
    }
}
