package com.sendback.domain.comment.controller;

import com.sendback.domain.comment.dto.request.SaveCommentRequestDto;
import com.sendback.domain.comment.dto.response.GetCommentsResponseDto;
import com.sendback.domain.comment.dto.response.SaveCommentResponseDto;
import com.sendback.global.ControllerTest;
import com.sendback.global.WithMockCustomUser;
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
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CommentControllerTest extends ControllerTest {

    @Nested
    @DisplayName("댓글 등록")
    class saveComment {

        @Test
        @WithMockCustomUser
        @DisplayName("정상적인 요청이라면 성공을 반환한다.")
        public void saveComment_success() throws Exception {
            // given
            Long mock_userId = 1L;
            Long mock_projectId = 1L;
            LocalDateTime mockDateTime = LocalDateTime.now();
            SaveCommentRequestDto commentRequestDto = new SaveCommentRequestDto("테스트");
            SaveCommentResponseDto saveCommentResponseDto = new SaveCommentResponseDto(1L, "테스트", 1L, "테스트 이미지",
                    "유저", mockDateTime);
            given(commentService.saveComment(mock_userId, mock_projectId, commentRequestDto)).willReturn(saveCommentResponseDto);

            String content = objectMapper.writeValueAsString(commentRequestDto);

            // when
            ResultActions resultActions = mockMvc.perform(post("/api/{projectId}/comments", 1L)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(content).with(csrf().asHeader())
                                    .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX + "AccessToken"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("200"))
                    .andExpect(jsonPath("$.message").value("성공"))
                    .andExpect(jsonPath("$.data.commentId").value(saveCommentResponseDto.commentId()))
                    .andExpect(jsonPath("$.data.content").value(saveCommentResponseDto.content()))
                    .andExpect(jsonPath("$.data.userId").value(saveCommentResponseDto.userId()))
                    .andExpect(jsonPath("$.data.profileImageUrl").value(saveCommentResponseDto.profileImageUrl()))
                    .andExpect(jsonPath("$.data.nickname").value(saveCommentResponseDto.nickname()))
                    .andExpect(jsonPath("$.data.createdAt").value(mockDateTime.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))))
                    .andDo(print());

            // then
            resultActions.andDo(document("saveComment-success",
                    customRequestPreprocessor(),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                            parameterWithName("projectId").description("프로젝트 ID")
                    ),
                    requestFields(
                            fieldWithPath("content").type(JsonFieldType.STRING)
                                    .description("내용")
                      ),
                    responseFields(
                            fieldWithPath("code").type(JsonFieldType.NUMBER)
                                    .description("코드"),
                            fieldWithPath("data").type(JsonFieldType.OBJECT)
                                    .description("응답 데이터"),
                            fieldWithPath("data.commentId").type(JsonFieldType.NUMBER)
                                    .description("댓글 ID"),
                            fieldWithPath("data.content").type(JsonFieldType.STRING)
                                    .description("내용"),
                            fieldWithPath("data.userId").type(JsonFieldType.NUMBER)
                                    .description("유저 ID"),
                            fieldWithPath("data.profileImageUrl").type(JsonFieldType.STRING)
                                    .description("프로필 이미지 url"),
                            fieldWithPath("data.nickname").type(JsonFieldType.STRING)
                                    .description("닉네임"),
                            fieldWithPath("data.createdAt").type(JsonFieldType.STRING)
                                    .attributes(Attributes.key("format").value("yyyy.MM.dd"))
                                    .description("등록 일자"),
                            fieldWithPath("message").type(JsonFieldType.STRING)
                                    .description("메시지")
                    )));

            verify(commentService).saveComment(any(), any(), any());
        }
    }

    @Nested
    @DisplayName("댓글 리스트 조회")
    class getCommentList {

        @Test
        @WithMockCustomUser
        @DisplayName("정상적인 요청이라면 댓글 리스트들을 조회한다.")
        public void getCommentList_success() throws Exception {
            // given
            Long mock_userId = 1L;
            Long mock_projectId = 1L;
            LocalDateTime mockDateTime = LocalDateTime.now();
            List<GetCommentsResponseDto> commentsResponseDtoList = new ArrayList<>();
            GetCommentsResponseDto responseDto = new GetCommentsResponseDto(1L, "테스터", "테스트 이미지", 1L,
                    "안녕하세요", mockDateTime, true);
            commentsResponseDtoList.add(responseDto);
            given(commentService.getCommentList(mock_userId, mock_projectId)).willReturn(commentsResponseDtoList);

            // when
            ResultActions resultActions = mockMvc.perform(get("/api/{projectId}/comments", 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(csrf().asHeader()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("200"))
                    .andExpect(jsonPath("$.message").value("성공"))
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data[0].commentId").value(commentsResponseDtoList.get(0).commentId()))
                    .andExpect(jsonPath("$.data[0].content").value(commentsResponseDtoList.get(0).content()))
                    .andExpect(jsonPath("$.data[0].userId").value(commentsResponseDtoList.get(0).userId()))
                    .andExpect(jsonPath("$.data[0].profileImageUrl").value(commentsResponseDtoList.get(0).profileImageUrl()))
                    .andExpect(jsonPath("$.data[0].nickname").value(commentsResponseDtoList.get(0).nickname()))
                    .andExpect(jsonPath("$.data[0].createdAt").value(mockDateTime.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))))
                    .andExpect(jsonPath("$.data[0].isAuthor").value(commentsResponseDtoList.get(0).isAuthor()))
                    .andDo(print());

            // then
            resultActions.andDo(document("getCommentList_success",
                    customRequestPreprocessor(),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                            parameterWithName("projectId").description("프로젝트 ID")
                    ),
                    responseFields(
                            fieldWithPath("code").type(JsonFieldType.NUMBER)
                                    .description("코드"),
                            fieldWithPath("data").type(JsonFieldType.ARRAY)
                                    .description("응답 데이터"),
                            fieldWithPath("data[0].userId").type(JsonFieldType.NUMBER)
                                    .description("유저 ID"),
                            fieldWithPath("data[0].nickname").type(JsonFieldType.STRING)
                                    .description("닉네임"),
                            fieldWithPath("data[0].profileImageUrl").type(JsonFieldType.STRING)
                                    .description("프로필 사진 URL"),
                            fieldWithPath("data[0].commentId").type(JsonFieldType.NUMBER)
                                    .description("댓글 ID"),
                            fieldWithPath("data[0].content").type(JsonFieldType.STRING)
                                    .description("내용"),
                            fieldWithPath("data[0].createdAt").type(JsonFieldType.STRING)
                                    .attributes(Attributes.key("format").value("yyyy.MM.dd"))
                                    .description("등록 일자"),
                            fieldWithPath("data[0].isAuthor").type(JsonFieldType.BOOLEAN)
                                    .description("작성자"),
                            fieldWithPath("message").type(JsonFieldType.STRING)
                                    .description("메시지")
                    )));

            verify(commentService).getCommentList(any(), any());
        }
    }
}
