package com.sendback.domain.feedback.controller;

import com.sendback.domain.feedback.dto.request.SaveFeedbackRequest;
import com.sendback.domain.feedback.dto.response.FeedbackDetailResponse;
import com.sendback.domain.feedback.dto.response.FeedbackIdResponse;
import com.sendback.domain.feedback.dto.response.SubmitFeedbackResponse;
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

        SaveFeedbackRequest saveFeedbackRequest = MOCK_SAVE_FEEDBACK_REQUEST;

        @Test
        @WithMockCustomUser
        @DisplayName("정상적인 요청이라면 성공을 반환한다.")
        public void success() throws Exception {
            //given
            FeedbackIdResponse response = new FeedbackIdResponse(1L);
            given(feedbackService.saveFeedback(anyLong(), anyLong(), any())).willReturn(response);

            //when
            ResultActions resultActions = mockMvc.perform(post("/api/projects/{projectId}/feedback", 1L)
                            .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX + "AccessToken")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(saveFeedbackRequest)).with(csrf()))
                    .andDo(print());

            //then
            resultActions
                    .andDo(document("feedback/save",
                            preprocessRequest(prettyPrint()),
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

        FeedbackDetailResponse mockFeedbackDetailResponse = MOCK_FEEDBACK_DETAIL_RESPONSE;

        @Test
        @WithMockCustomUser
        @DisplayName("정상적인 접근 시 성공을 반환한다.")
        public void success() throws Exception {
            //given
            Long projectId = 1L;
            Long feedbackId = 1L;
            given(feedbackService.getFeedbackDetail(anyLong(), anyLong())).willReturn(mockFeedbackDetailResponse);

            //when
            ResultActions resultActions = mockMvc.perform(get("/api/projects/{projectId}/feedbacks/{feedbackId}", projectId, feedbackId).with(csrf()))
                    .andDo(print());

            //then
            resultActions
                    .andDo(document("feedback/detail",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            pathParameters(
                                    parameterWithName("projectId").description("프로젝트 ID"),
                                    parameterWithName("feedbackId").description("피드백 ID")
                            ),
                            responseFields(
                                    fieldWithPath("code").type(JsonFieldType.NUMBER)
                                            .description("코드"),
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
                                    fieldWithPath("data.field").type(JsonFieldType.STRING).description("분야"),
                                    fieldWithPath("data.progress").type(JsonFieldType.STRING).description("진행 정도"),
                                    fieldWithPath("message").type(JsonFieldType.STRING)
                                            .description("메시지")
                            )))
                    .andExpect(jsonPath("$.code").value("200"))
                    .andExpect(jsonPath("$.message").value("성공"))
                    .andExpect(jsonPath("$.data.username").value(mockFeedbackDetailResponse.username()))
                    .andExpect(jsonPath("$.data.userLevel").value(mockFeedbackDetailResponse.userLevel()))
                    .andExpect(jsonPath("$.data.profileImageUrl").value(mockFeedbackDetailResponse.profileImageUrl()))
                    .andExpect(jsonPath("$.data.feedbackId").value(mockFeedbackDetailResponse.feedbackId()))
                    .andExpect(jsonPath("$.data.feedbackTitle").value(mockFeedbackDetailResponse.feedbackTitle()))
                    .andExpect(jsonPath("$.data.linkUrl").value(mockFeedbackDetailResponse.linkUrl()))
                    .andExpect(jsonPath("$.data.content").value(mockFeedbackDetailResponse.content()))
                    .andExpect(jsonPath("$.data.rewardMessage").value(mockFeedbackDetailResponse.rewardMessage()))
                    .andExpect(jsonPath("$.data.createdAt").value(mockFeedbackDetailResponse.createdAt()))
                    .andExpect(jsonPath("$.data.startedAt").value(mockFeedbackDetailResponse.startedAt()))
                    .andExpect(jsonPath("$.data.endedAt").value(mockFeedbackDetailResponse.endedAt()))
                    .andExpect(jsonPath("$.data.projectId").value(mockFeedbackDetailResponse.projectId()))
                    .andExpect(jsonPath("$.data.projectTitle").value(mockFeedbackDetailResponse.projectTitle()))
                    .andExpect(jsonPath("$.data.field").value(mockFeedbackDetailResponse.field()))
                    .andExpect(jsonPath("$.data.progress").value(mockFeedbackDetailResponse.progress()))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("피드백을 제출 시")
    class submitFeedback {

        SubmitFeedbackResponse submitFeedbackResponse = MOCK_SUBMIT_FEEDBACK_RESPONSE;
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

            given(feedbackService.submitFeedback(anyLong(), anyLong(), any())).willReturn(submitFeedbackResponse);


            //when
            ResultActions resultActions = mockMvc.perform(multipart("/api/projects/{projectId}/feedbacks/{feedbackId}", projectId, feedbackId)
                    .file(mockMultipartFile)
                    .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX + "AccessToken")
                    .contentType(MediaType.MULTIPART_FORM_DATA).with(csrf()));
            resultActions
                    .andDo(print());

            //then
            resultActions
                    .andDo(document("feedback/submit",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            pathParameters(
                                    parameterWithName("projectId").description("프로젝트 ID"),
                                    parameterWithName("feedbackId").description("피드백 ID")
                            ),
                            requestHeaders(
                                    headerWithName("Authorization").description("JWT 엑세스 토큰")
                            ),
                            responseFields(
                                    fieldWithPath("code").type(JsonFieldType.NUMBER)
                                            .description("코드"),
                                    fieldWithPath("data.level").type(JsonFieldType.STRING).description("유저 레벨"),
                                    fieldWithPath("data.isLevelUp").type(JsonFieldType.BOOLEAN).description("레벨업 했는지 여부"),
                                    fieldWithPath("data.remainFeedbackCount").type(JsonFieldType.NUMBER).description("레벨업까지 남은 피드백 횟수"),
                                    fieldWithPath("message").type(JsonFieldType.STRING)
                                            .description("메시지")
                            )))
                    .andExpect(jsonPath("$.code").value("200"))
                    .andExpect(jsonPath("$.message").value("성공"))
                    .andExpect(jsonPath("$.data.level").value(submitFeedbackResponse.level()))
                    .andExpect(jsonPath("$.data.isLevelUp").value(submitFeedbackResponse.isLevelUp()))
                    .andExpect(jsonPath("$.data.remainFeedbackCount").value(submitFeedbackResponse.remainFeedbackCount()))
                    .andExpect(status().isOk());


        }
    }
}