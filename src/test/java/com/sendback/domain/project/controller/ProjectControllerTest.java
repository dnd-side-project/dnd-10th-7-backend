package com.sendback.domain.project.controller;

import com.sendback.domain.project.dto.request.SaveProjectRequestDto;
import com.sendback.domain.project.dto.request.UpdateProjectRequestDto;
import com.sendback.domain.project.dto.response.ProjectIdResponseDto;
import com.sendback.global.ControllerTest;
import com.sendback.global.WithMockCustomUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import static com.sendback.domain.project.fixture.ProjectFixture.MOCK___SAVE_PROJECT_REQUEST_DTO;
import static com.sendback.domain.project.fixture.ProjectFixture.MOCK___UPDATE_PROJECT_REQUEST_DTO;
import static org.mockito.ArgumentMatchers.*;


import java.nio.charset.StandardCharsets;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProjectControllerTest extends ControllerTest {

    @Nested
    @DisplayName("project 등록 요청 시")
    class saveProject {

        SaveProjectRequestDto saveProjectRequestDto = MOCK___SAVE_PROJECT_REQUEST_DTO;
        @Test
        @DisplayName("saveProjectRequest와 이미지들이 존재한다면 성공을 반환한다.")
        @WithMockCustomUser
        public void saveProject_success() throws Exception {
            //given
            MockMultipartFile first_multipartFile = mockingMultipartFile("first");
            MockMultipartFile second_multipartFile = mockingMultipartFile("second");
            MockMultipartFile data =
                    new MockMultipartFile("data", null, "application/json",
                            objectMapper.writeValueAsString(saveProjectRequestDto).getBytes(StandardCharsets.UTF_8));
            ProjectIdResponseDto projectIdResponseDto = new ProjectIdResponseDto(1L);
            given(projectService.saveProject(anyLong(), any(SaveProjectRequestDto.class), anyList())).willReturn(projectIdResponseDto);

            //when
            ResultActions resultActions = mockMvc.perform(multipart("/api/projects")
                            .file("images", first_multipartFile.getBytes())
                            .file("images", second_multipartFile.getBytes())
                            .file(data)
                            .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX + "AccessToken")
                            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                            .accept(MediaType.APPLICATION_JSON).with(csrf()))
                    .andExpect(jsonPath("$.code").value("200"))
                    .andExpect(jsonPath("$.message").value("성공"))
                    .andExpect(jsonPath("$.data.projectId").value(1))
                    .andDo(print());

            //then
            resultActions
                    .andDo(document("project/save",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            requestHeaders(
                                    headerWithName("Authorization").description("JWT 엑세스 토큰")
                            ),
                            requestPartFields(
                                    "data",
                                    fieldWithPath("title").type(JsonFieldType.STRING).description("프로젝트 제목"),
                                    fieldWithPath("field").type(JsonFieldType.STRING).description("분야"),
                                    fieldWithPath("content").type(JsonFieldType.STRING).description("내용"),
                                    fieldWithPath("summary").type(JsonFieldType.STRING).description("한 줄 요약"),
                                    fieldWithPath("demoSiteUrl").type(JsonFieldType.STRING).description("데모 사이트 주소"),
                                    fieldWithPath("startedAt").type(JsonFieldType.STRING).description("시작 날짜"),
                                    fieldWithPath("endedAt").type(JsonFieldType.STRING).description("끝나는 날짜"),
                                    fieldWithPath("progress").type(JsonFieldType.STRING).description("진행 정도"),
                                    fieldWithPath("plannerCount").type(JsonFieldType.NUMBER).description("기획자 인원"),
                                    fieldWithPath("frontendCount").type(JsonFieldType.NUMBER).description("프론트엔드 인원"),
                                    fieldWithPath("backendCount").type(JsonFieldType.NUMBER).description("백엔드 인원"),
                                    fieldWithPath("designCount").type(JsonFieldType.NUMBER).description("디자인 인원")
                            ),
                            requestParts(
                                partWithName("data").description("프로젝트 정보"),
                                partWithName("images").description("프로젝트 첨부 파일")
                            ),
                            responseFields(
                                    fieldWithPath("code").type(JsonFieldType.NUMBER)
                                            .description("코드"),
                                    fieldWithPath("data.projectId").type(JsonFieldType.NUMBER)
                                            .description("프로젝트 ID"),
                                    fieldWithPath("message").type(JsonFieldType.STRING)
                                            .description("메시지")
                            )))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("saveProjectRequest가 존재한다면 성공을 반환한다.")
        @WithMockCustomUser
        public void saveProject_success_onlySaveProjectRequest() throws Exception {
            //given
            MockMultipartFile data =
                    new MockMultipartFile("data", null, "application/json",
                            objectMapper.writeValueAsString(saveProjectRequestDto).getBytes(StandardCharsets.UTF_8));
            ProjectIdResponseDto projectIdResponseDto = new ProjectIdResponseDto(1L);
            given(projectService.saveProject(anyLong(), any(SaveProjectRequestDto.class), any())).willReturn(projectIdResponseDto);

            //when
            mockMvc.perform(multipart("/api/projects")
                            .file(data)
                            .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX+"AccessToken")
                            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                            .accept(MediaType.APPLICATION_JSON).with(csrf()))
                    .andDo(print())
                    .andExpect(jsonPath("$.code").value("200"))
                    .andExpect(jsonPath("$.message").value("성공"))
                    .andExpect(jsonPath("$.data.projectId").value(1))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("saveProjectRequest가 존재하지 않는다면 프로젝트를 등록할 때 에러를 일으킨다.")
        @WithMockCustomUser
        public void saveProject_fail_request() throws Exception {
            //when
            ResultActions resultActions = mockMvc.perform(multipart("/api/projects")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX + "AccessToken")
                            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                            .accept(MediaType.APPLICATION_JSON).with(csrf()))
                    .andDo(print());

            //then
            resultActions
                    .andDo(document("project/save/failByNotExistData",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            requestHeaders(
                                    headerWithName("Authorization").description("JWT 엑세스 토큰")
                            )))
                    .andExpect(jsonPath("$.code").value("302"))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("project 수정 요청 시")
    class updateProject {

        UpdateProjectRequestDto updateProjectRequestDto = MOCK___UPDATE_PROJECT_REQUEST_DTO;

        @Test
        @DisplayName("updateProjectRequest와 이미지들이 존재한다면 성공을 반환한다.")
        @WithMockCustomUser
        public void updateProject_success() throws Exception {
            //given
            Long projectId = 1L;

            MockMultipartFile first_multipartFile = mockingMultipartFile("first");
            MockMultipartFile second_multipartFile = mockingMultipartFile("second");
            MockMultipartFile data =
                    new MockMultipartFile("data", null, "application/json",
                            objectMapper.writeValueAsString(updateProjectRequestDto).getBytes(StandardCharsets.UTF_8));
            ProjectIdResponseDto projectIdResponseDto = new ProjectIdResponseDto(projectId);
            given(projectService.updateProject(anyLong(), anyLong(), any(UpdateProjectRequestDto.class), anyList())).willReturn(projectIdResponseDto);

            //when
            ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.
                            multipart("/api/projects/{projectId}", projectId)
                            .file("images", first_multipartFile.getBytes())
                            .file("images", second_multipartFile.getBytes())
                            .file(data)
                            .with(request -> {
                                request.setMethod("PUT");
                                return request;
                            })
                            .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX + "AccessToken")
                            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                            .accept(MediaType.APPLICATION_JSON).with(csrf()))
                    .andDo(print());

            //then
            resultActions
                    .andDo(document("project/update",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            pathParameters(
                                    parameterWithName("projectId").description("프로젝트 ID")
                            ),
                            requestHeaders(
                                    headerWithName("Authorization").description("JWT 엑세스 토큰")
                            ),
                            requestPartFields(
                                    "data",
                                    fieldWithPath("title").type(JsonFieldType.STRING).description("프로젝트 제목"),
                                    fieldWithPath("field").type(JsonFieldType.STRING).description("분야"),
                                    fieldWithPath("content").type(JsonFieldType.STRING).description("내용"),
                                    fieldWithPath("summary").type(JsonFieldType.STRING).description("한 줄 요약"),
                                    fieldWithPath("demoSiteUrl").type(JsonFieldType.STRING).description("데모 사이트 주소"),
                                    fieldWithPath("startedAt").type(JsonFieldType.STRING).description("시작 날짜"),
                                    fieldWithPath("endedAt").type(JsonFieldType.STRING).description("끝나는 날짜"),
                                    fieldWithPath("progress").type(JsonFieldType.STRING).description("진행 정도"),
                                    fieldWithPath("plannerCount").type(JsonFieldType.NUMBER).description("기획자 인원"),
                                    fieldWithPath("frontendCount").type(JsonFieldType.NUMBER).description("프론트엔드 인원"),
                                    fieldWithPath("backendCount").type(JsonFieldType.NUMBER).description("백엔드 인원"),
                                    fieldWithPath("designCount").type(JsonFieldType.NUMBER).description("디자인 인원"),
                                    fieldWithPath("urlsToDelete").type(JsonFieldType.ARRAY).description("삭제할 사진 URL")
                            ),
                            requestParts(
                                    partWithName("data").description("업데이트 프로젝트 정보"),
                                    partWithName("images").description("프로젝트 첨부 파일")
                            ),
                            responseFields(
                                    fieldWithPath("code").type(JsonFieldType.NUMBER)
                                            .description("코드"),
                                    fieldWithPath("data.projectId").type(JsonFieldType.NUMBER)
                                            .description("프로젝트 ID"),
                                    fieldWithPath("message").type(JsonFieldType.STRING)
                                            .description("메시지")
                            )))
                    .andExpect(jsonPath("$.code").value("200"))
                    .andExpect(jsonPath("$.message").value("성공"))
                    .andExpect(jsonPath("$.data.projectId").value(projectId))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("updateProjectRequest가 존재하지 않는다면 프로젝트를 등록할 때 에러를 일으킨다.")
        @WithMockCustomUser
        public void updateProject_fail_request() throws Exception {
            //given
            Long projectId = 1L;

            //when
            ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.
                            multipart("/api/projects/{projectId}", projectId)
                            .with(request -> {
                                request.setMethod("PUT");
                                return request;
                            })
                            .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX + "AccessToken")
                            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                            .accept(MediaType.APPLICATION_JSON).with(csrf()))
                    .andDo(print());

            //then
            resultActions
                    .andDo(document("project/update/failByNotExistData",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            pathParameters(
                                    parameterWithName("projectId").description("프로젝트 ID")
                            ),
                            requestHeaders(
                                    headerWithName("Authorization").description("JWT 엑세스 토큰")
                            ),
                            responseFields(
                                    fieldWithPath("code").type(JsonFieldType.NUMBER)
                                            .description("코드"),
                                    fieldWithPath("message").type(JsonFieldType.STRING)
                                            .description("에러 메시지"),
                                    fieldWithPath("data").type(JsonFieldType.NULL)
                                            .description("데이터")
                            )))
                    .andExpect(jsonPath("$.code").value("302"))
                    .andExpect(status().isBadRequest());
        }

    }

    @Nested
    @DisplayName("프로젝트 삭제 시")
    class deleteProject {

        @Test
        @DisplayName("정상적인 요청이라면 프로젝트를 삭제한다.")
        @WithMockCustomUser
        public void deleteProject_success() throws Exception {
            //given
            Long projectId = 1L;
            doNothing().when(projectService).deleteProject(anyLong(), anyLong());

            //when
            ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/projects/{projectId}", projectId)
                            .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX + "AccessToken")
                            .accept(MediaType.APPLICATION_JSON).with(csrf()))
                    .andDo(print());

            //then
            resultActions
                    .andDo(document("project/delete",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            pathParameters(
                                    parameterWithName("projectId").description("프로젝트 ID")
                            ),
                            requestHeaders(
                                    headerWithName("Authorization").description("JWT 엑세스 토큰")
                            ),
                            responseFields(
                                    fieldWithPath("code").type(JsonFieldType.NUMBER)
                                            .description("코드"),
                                    fieldWithPath("data").type(JsonFieldType.NULL)
                                            .description("데이터"),
                                    fieldWithPath("message").type(JsonFieldType.STRING)
                                            .description("메시지")
                            )))
                    .andExpect(status().isOk());
        }
    }

}
