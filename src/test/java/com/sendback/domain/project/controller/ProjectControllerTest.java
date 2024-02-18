package com.sendback.domain.project.controller;

import com.sendback.domain.project.dto.request.SaveProjectRequestDto;
import com.sendback.domain.project.dto.request.UpdateProjectRequestDto;
import com.sendback.domain.project.dto.response.GetProjectsResponseDto;
import com.sendback.domain.project.dto.response.ProjectDetailResponseDto;
import com.sendback.domain.project.dto.response.ProjectIdResponseDto;
import com.sendback.domain.project.dto.response.RecommendedProjectResponseDto;
import com.sendback.domain.project.dto.response.PullUpProjectResponseDto;
import com.sendback.global.ControllerTest;
import com.sendback.global.WithMockCustomUser;
import com.sendback.global.common.CustomPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.test.web.servlet.ResultActions;
import static com.sendback.domain.project.fixture.ProjectFixture.*;
import static org.mockito.ArgumentMatchers.*;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
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
    @DisplayName("project 메인 페이지 조회 api")
    class getProjects {

        GetProjectsResponseDto getProjectsResponseDto = MOCK_GET_PROJECTS_RESPONSE_DTO;
        @Test
        @WithMockCustomUser
        @DisplayName("정상적인 요청일 시 성공을 반환한다.")
        public void success() throws Exception {
            //given
            CustomPage<GetProjectsResponseDto> customPage = CustomPage.<GetProjectsResponseDto>builder()
                    .page(1)
                    .size(5)
                    .totalElements(100L)
                    .totalPages(10)
                    .content(List.of(getProjectsResponseDto))
                    .build();

            given(projectService.getProjects(any(), any(Pageable.class), any(), any(), any(), any())).willReturn(customPage);

            //when
            ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get("/api/projects")
                            .with(csrf().asHeader())
                            .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX + "AccessToken")
                            .characterEncoding(StandardCharsets.UTF_8)
                            .param("page", "1")
                            .param("size", "5")
                            .param("keyword", "재미있는")
                            .param("field", "게임")
                            .param("is-finished", "true")
                            .param("sort", "0"))
                    .andDo(print());

            //then
            resultActions
                    .andDo(document("project/select",
                            customRequestPreprocessor(),
                            preprocessResponse(prettyPrint()),
                            queryParameters(
                                    parameterWithName("page").description("페이지"),
                                    parameterWithName("size").description("사이즈"),
                                    parameterWithName("keyword").description("검색 키워드"),
                                    parameterWithName("field").description("분야"),
                                    parameterWithName("is-finished").description("종료 여부"),
                                    parameterWithName("sort").description("정렬 기준")

                            ),
                            requestHeaders(
                                    headerWithName("Authorization").description("JWT 엑세스 토큰").optional()
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
                                    fieldWithPath("data.content[].nickname").type(JsonFieldType.STRING)
                                            .description("작성자 닉네임"),
                                    fieldWithPath("data.content[].profileImageUrl").type(JsonFieldType.STRING)
                                            .description("작성자 프로필사진"),
                                    fieldWithPath("data.content[].projectId").type(JsonFieldType.NUMBER)
                                            .description("프로젝트 ID"),
                                    fieldWithPath("data.content[].title").type(JsonFieldType.STRING)
                                            .description("프로젝트 제목"),
                                    fieldWithPath("data.content[].summary").type(JsonFieldType.STRING)
                                            .description("프로젝트 한 줄 요약"),
                                    fieldWithPath("data.content[].progress").type(JsonFieldType.STRING)
                                            .description("프로젝트 진행 정도"),
                                    fieldWithPath("data.content[].field").type(JsonFieldType.STRING)
                                            .description("프로젝트 분야"),
                                    fieldWithPath("data.content[].createdAt").type(JsonFieldType.STRING)
                                            .attributes(Attributes.key("format").value("yyyy.MM.dd"))
                                            .description("프로젝트 등록일"),
                                    fieldWithPath("data.content[].pullUpCount").type(JsonFieldType.NUMBER)
                                            .description("Pull Up 수"),
                                    fieldWithPath("data.content[].likeCount").type(JsonFieldType.NUMBER)
                                            .description("좋아요 수"),
                                    fieldWithPath("data.content[].commentCount").type(JsonFieldType.NUMBER)
                                            .description("댓글 수"),
                                    fieldWithPath("data.content[].isScrapped").type(JsonFieldType.BOOLEAN)
                                            .description("스크랩 여부")
                            )))
                    .andExpect(jsonPath("$.code").value("200"))
                    .andExpect(jsonPath("$.message").value("성공"))
                    .andExpect(jsonPath("$.data.page").value(customPage.getPage()))
                    .andExpect(jsonPath("$.data.size").value(customPage.getSize()))
                    .andExpect(jsonPath("$.data.totalElements").value(customPage.getTotalElements()))
                    .andExpect(jsonPath("$.data.totalPages").value(customPage.getTotalPages()))
                    .andExpect(jsonPath("$.data.content").isArray())
                    .andExpect(jsonPath("$.data.content[0].nickname").value(getProjectsResponseDto.nickname()))
                    .andExpect(jsonPath("$.data.content[0].profileImageUrl").value(getProjectsResponseDto.profileImageUrl()))
                    .andExpect(jsonPath("$.data.content[0].projectId").value(getProjectsResponseDto.projectId()))
                    .andExpect(jsonPath("$.data.content[0].title").value(getProjectsResponseDto.title()))
                    .andExpect(jsonPath("$.data.content[0].summary").value(getProjectsResponseDto.summary()))
                    .andExpect(jsonPath("$.data.content[0].progress").value(getProjectsResponseDto.progress()))
                    .andExpect(jsonPath("$.data.content[0].field").value(getProjectsResponseDto.field()))
                    .andExpect(jsonPath("$.data.content[0].createdAt").value(getProjectsResponseDto.createdAt()
                            .format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))))
                    .andExpect(jsonPath("$.data.content[0].pullUpCount").value(getProjectsResponseDto.pullUpCount()))
                    .andExpect(jsonPath("$.data.content[0].likeCount").value(getProjectsResponseDto.likeCount()))
                    .andExpect(jsonPath("$.data.content[0].commentCount").value(getProjectsResponseDto.commentCount()))
                    .andExpect(jsonPath("$.data.content[0].isScrapped").value(getProjectsResponseDto.isScrapped()))
                    .andExpect(status().isOk());
        }

    }

    @Nested
    @DisplayName("project 상세 조회 시")
    class getProjectDetail {

        ProjectDetailResponseDto projectDetailResponseDto = MOCK_PROJECT_DETAIL_RESPONSE_DTO;

        @Test
        @DisplayName("정상적인 요청일 시 성공을 반환한다.")
        @WithMockCustomUser
        public void success() throws Exception {
            //given
            Long projectId = 1L;
            given(projectService.getProjectDetail(any(), any())).willReturn(projectDetailResponseDto);

            //when
            ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get("/api/projects/{projectId}", projectId)
                            .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX + "AccessToken")
                            .with(csrf().asHeader()))
                    .andDo(print());

            //then
            resultActions
                    .andDo(document("project/detail",
                            customRequestPreprocessor(),
                            preprocessResponse(prettyPrint()),
                            requestHeaders(
                                    headerWithName("Authorization").description("JWT 엑세스 토큰").optional()
                            ),
                            pathParameters(
                                    parameterWithName("projectId").description("프로젝트 ID")
                            ),
                            responseFields(
                                    fieldWithPath("code").type(JsonFieldType.NUMBER)
                                            .description("코드"),
                                    fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                                    fieldWithPath("data.userId").type(JsonFieldType.NUMBER).description("유저 ID"),
                                    fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("유저 이름"),
                                    fieldWithPath("data.userLevel").type(JsonFieldType.STRING).description("유저 레벨"),
                                    fieldWithPath("data.profileImageUrl").type(JsonFieldType.STRING).description("프로필 이미지 주소"),
                                    fieldWithPath("data.projectId").type(JsonFieldType.NUMBER).description("프로젝트 ID"),
                                    fieldWithPath("data.title").type(JsonFieldType.STRING).description("제목"),
                                    fieldWithPath("data.field").type(JsonFieldType.STRING).description("분야"),
                                    fieldWithPath("data.content").type(JsonFieldType.STRING).description("내용"),
                                    fieldWithPath("data.demoSiteUrl").type(JsonFieldType.STRING).description("데모 사이트 주소"),
                                    fieldWithPath("data.progress").type(JsonFieldType.STRING).description("진행 정도"),
                                    fieldWithPath("data.projectImageUrl").type(JsonFieldType.ARRAY).description("프로젝트 사진"),
                                    fieldWithPath("data.frontendCount").type(JsonFieldType.NUMBER).description("프론트엔드 인원"),
                                    fieldWithPath("data.backendCount").type(JsonFieldType.NUMBER).description("백엔드 인원"),
                                    fieldWithPath("data.designerCount").type(JsonFieldType.NUMBER).description("디자이너 인원"),
                                    fieldWithPath("data.plannerCount").type(JsonFieldType.NUMBER).description("기획자 인원"),
                                    fieldWithPath("data.likeCount").type(JsonFieldType.NUMBER).description("좋아요 수"),
                                    fieldWithPath("data.scrapCount").type(JsonFieldType.NUMBER).description("스크랩 수"),
                                    fieldWithPath("data.commentCount").type(JsonFieldType.NUMBER).description("댓글 수"),
                                    fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("글 생성 날짜")
                                            .attributes(Attributes.key("format").value("yyyy.MM.dd hh:mm")),
                                    fieldWithPath("data.startedAt").type(JsonFieldType.STRING).description("시작 날짜")
                                            .attributes(Attributes.key("format").value("yyyy.MM.dd")),
                                    fieldWithPath("data.endedAt").type(JsonFieldType.STRING).description("끝나는 날짜")
                                            .attributes(Attributes.key("format").value("yyyy.MM.dd")),
                                    fieldWithPath("data.isAuthor").type(JsonFieldType.BOOLEAN).description("작성자인지 여부"),
                                    fieldWithPath("data.isCheckedLike").type(JsonFieldType.BOOLEAN).description("좋아요 체크 여부"),
                                    fieldWithPath("data.isCheckedScrap").type(JsonFieldType.BOOLEAN).description("스크랩 체크 여부"),
                                    fieldWithPath("message").type(JsonFieldType.STRING)
                                            .description("메시지")
                            )))
                    .andExpect(jsonPath("$.code").value("200"))
                    .andExpect(jsonPath("$.message").value("성공"))
                    .andExpect(jsonPath("$.data.userId").value(projectDetailResponseDto.userId()))
                    .andExpect(jsonPath("$.data.nickname").value(projectDetailResponseDto.nickname()))
                    .andExpect(jsonPath("$.data.userLevel").value(projectDetailResponseDto.userLevel()))
                    .andExpect(jsonPath("$.data.profileImageUrl").value(projectDetailResponseDto.profileImageUrl()))
                    .andExpect(jsonPath("$.data.projectId").value(projectDetailResponseDto.projectId()))
                    .andExpect(jsonPath("$.data.title").value(projectDetailResponseDto.title()))
                    .andExpect(jsonPath("$.data.field").value(projectDetailResponseDto.field()))
                    .andExpect(jsonPath("$.data.content").value(projectDetailResponseDto.content()))
                    .andExpect(jsonPath("$.data.demoSiteUrl").value(projectDetailResponseDto.demoSiteUrl()))
                    .andExpect(jsonPath("$.data.progress").value(projectDetailResponseDto.progress()))
                    .andExpect(jsonPath("$.data.frontendCount").value(projectDetailResponseDto.frontendCount()))
                    .andExpect(jsonPath("$.data.backendCount").value(projectDetailResponseDto.backendCount()))
                    .andExpect(jsonPath("$.data.designerCount").value(projectDetailResponseDto.designerCount()))
                    .andExpect(jsonPath("$.data.plannerCount").value(projectDetailResponseDto.plannerCount()))
                    .andExpect(jsonPath("$.data.likeCount").value(projectDetailResponseDto.likeCount()))
                    .andExpect(jsonPath("$.data.scrapCount").value(projectDetailResponseDto.scrapCount()))
                    .andExpect(jsonPath("$.data.commentCount").value(projectDetailResponseDto.commentCount()))
                    .andExpect(jsonPath("$.data.createdAt").value(projectDetailResponseDto.createdAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd hh:mm"))))
                    .andExpect(jsonPath("$.data.startedAt").value(projectDetailResponseDto.startedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))))
                    .andExpect(jsonPath("$.data.endedAt").value(projectDetailResponseDto.endedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))))
                    .andExpect(jsonPath("$.data.isAuthor").value(projectDetailResponseDto.isAuthor()))
                    .andExpect(jsonPath("$.data.isCheckedLike").value(projectDetailResponseDto.isCheckedLike()))
                    .andExpect(jsonPath("$.data.isCheckedScrap").value(projectDetailResponseDto.isCheckedScrap()))
                    .andExpect(status().isOk());

        }
    }

    @Nested
    @DisplayName("project 등록 요청 시")
    class saveProject {

        SaveProjectRequestDto saveProjectRequestDto = MOCK_SAVE_PROJECT_REQUEST_DTO;
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
                            .accept(MediaType.APPLICATION_JSON).with(csrf().asHeader()))
                    .andExpect(jsonPath("$.code").value("200"))
                    .andExpect(jsonPath("$.message").value("성공"))
                    .andExpect(jsonPath("$.data.projectId").value(1))
                    .andDo(print());

            //then
            resultActions
                    .andDo(document("project/save",
                            customRequestPreprocessor(),
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
                                    fieldWithPath("data").type(JsonFieldType.OBJECT)
                                            .description("응답 데이터"),
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
                            .accept(MediaType.APPLICATION_JSON).with(csrf().asHeader()))
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
                            .accept(MediaType.APPLICATION_JSON).with(csrf().asHeader()))
                    .andDo(print());

            //then
            resultActions
                    .andDo(document("project/save/failByNotExistData",
                            customRequestPreprocessor(),
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

        UpdateProjectRequestDto updateProjectRequestDto = MOCK_UPDATE_PROJECT_REQUEST_DTO;

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
                            .accept(MediaType.APPLICATION_JSON).with(csrf().asHeader()))
                    .andDo(print());

            //then
            resultActions
                    .andDo(document("project/update",
                            customRequestPreprocessor(),
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
                                    fieldWithPath("data").type(JsonFieldType.OBJECT)
                                            .description("응답 데이터"),
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
                            .accept(MediaType.APPLICATION_JSON).with(csrf().asHeader()))
                    .andDo(print());

            //then
            resultActions
                    .andDo(document("project/update/failByNotExistData",
                            customRequestPreprocessor(),
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
            ResultActions resultActions = mockMvc.perform(delete("/api/projects/{projectId}", projectId)
                            .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX + "AccessToken")
                            .accept(MediaType.APPLICATION_JSON).with(csrf().asHeader()))
                    .andDo(print());

            //then
            resultActions
                    .andDo(document("project/delete",
                            customRequestPreprocessor(),
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

    @Nested
    @DisplayName("추천 프로젝트 리스트 조회")
    class getRecommendedProject {

        RecommendedProjectResponseDto recommendedProjectResponseDto = MOCK_RECOMMEND_PROJECT_RESPONSE_DTO;

        @Test
        @DisplayName("200 상태코드와 함께 추천 프로젝트를 반환한다.")
        @WithMockCustomUser
        public void getRecommended_success() throws Exception {
            // given
            List<RecommendedProjectResponseDto> responseDtos = new ArrayList<>();
            responseDtos.add(recommendedProjectResponseDto);

            given(projectService.getRecommendedProject(anyLong())).willReturn(responseDtos);

            // when
            ResultActions resultActions = mockMvc.perform(get("/api/projects/recommend")
                            .with(csrf().asHeader()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value("200"))
                    .andExpect(jsonPath("$.message").value("성공"))
                    .andExpect(jsonPath("$.data[0].projectId").value(responseDtos.get(0).projectId()))
                    .andExpect(jsonPath("$.data[0].progress").value(responseDtos.get(0).progress()))
                    .andExpect(jsonPath("$.data[0].field").value(responseDtos.get(0).field()))
                    .andExpect(jsonPath("$.data[0].title").value(responseDtos.get(0).title()))
                    .andExpect(jsonPath("$.data[0].summary").value(responseDtos.get(0).summary()))
                    .andExpect(jsonPath("$.data[0].createdBy").value(responseDtos.get(0).createdBy()))
                    .andExpect(jsonPath("$.data[0].createdAt").value(MOCK_LOCAL_DATE_TIME.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))))
                    .andExpect(jsonPath("$.data[0].profileImageUrl").value(responseDtos.get(0).profileImageUrl()))
                    .andDo(print());

            // then
            resultActions
                    .andDo(document("getRecommendedProjects-success",
                            customRequestPreprocessor(),
                            preprocessResponse(prettyPrint()),
                            responseFields(
                                    fieldWithPath("code").type(JsonFieldType.NUMBER)
                                            .description("상태 코드"),
                                    fieldWithPath("message").type(JsonFieldType.STRING)
                                            .description("메시지"),
                                    fieldWithPath("data[].projectId").type(JsonFieldType.NUMBER)
                                            .description("프로젝트 ID"),
                                    fieldWithPath("data[].progress").type(JsonFieldType.STRING)
                                            .description("진행 상태"),
                                    fieldWithPath("data[].field").type(JsonFieldType.STRING)
                                            .description("분야"),
                                    fieldWithPath("data[].title").type(JsonFieldType.STRING)
                                            .description("타이틀"),
                                    fieldWithPath("data[].summary").type(JsonFieldType.STRING)
                                            .description("줄거리"),
                                    fieldWithPath("data[].createdAt").type(JsonFieldType.STRING)
                                            .description("등록한 날짜"),
                                    fieldWithPath("data[].createdBy").type(JsonFieldType.STRING)
                                            .description("등록한 사람"),
                                    fieldWithPath("data[].profileImageUrl").type(JsonFieldType.STRING)
                                            .description("프로필 이미지")
                            )));
            verify(projectService).getRecommendedProject(anyLong());
        }
    }
    @Nested

    @DisplayName("프로젝트 끌올 시")
    class pullUpProject {

        PullUpProjectResponseDto pullUpProjectResponseDto = new PullUpProjectResponseDto(true);

        @Test
        @DisplayName("정상적인 요청이라면 프로젝트를 삭제한다.")
        @WithMockCustomUser
        public void success() throws Exception {
            //given
            Long projectId = 1L;
            given(projectService.pullUpProject(anyLong(), anyLong())).willReturn(pullUpProjectResponseDto);

            //when
            ResultActions resultActions = mockMvc.perform(put("/api/projects/{projectId}/pull-up", projectId)
                            .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX + "AccessToken")
                            .accept(MediaType.APPLICATION_JSON).with(csrf().asHeader()))
                    .andDo(print());

            //then
            resultActions
                    .andDo(document("project/pull-up",
                            customRequestPreprocessor(),
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
                                    fieldWithPath("data").type(JsonFieldType.OBJECT)
                                            .description("데이터"),
                                    fieldWithPath("data.isPulledUp").type(JsonFieldType.BOOLEAN)
                                            .description("끌올 여부 결과"),
                                    fieldWithPath("message").type(JsonFieldType.STRING)
                                            .description("메시지")
                            )))
                    .andExpect(jsonPath("$.code").value("200"))
                    .andExpect(jsonPath("$.message").value("성공"))
                    .andExpect(jsonPath("$.data.isPulledUp").value(true))
                    .andExpect(status().isOk());

        }
    }

}
