package com.sendback.domain.scrap.controller;

import com.sendback.domain.scrap.dto.response.ClickScrapResponseDto;
import com.sendback.global.ControllerTest;
import com.sendback.global.WithMockCustomUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ScrapControllerTest extends ControllerTest {

    @Test
    @WithMockCustomUser
    @DisplayName("스크랩을 클릭할 때 정상 요청이면 값을 반환한다.")
    public void success() throws Exception {
        //given
        Long projectId = 1L;
        ClickScrapResponseDto response = new ClickScrapResponseDto(true);
        given(scrapService.click(anyLong(), anyLong())).willReturn(response);

        //when
        ResultActions resultActions = mockMvc.perform(put("/api/projects/{projectId}/scrap", projectId)
                        .header(HttpHeaders.AUTHORIZATION, ACCESS_TOKEN_PREFIX + "AccessToken")
                        .accept(MediaType.APPLICATION_JSON).with(csrf().asHeader()))
                .andDo(print());

        //then
        resultActions
                .andDo(document("scrap/click",
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
                                        .description("응답 데이터"),
                                fieldWithPath("data.isClicked").type(JsonFieldType.BOOLEAN)
                                        .description("데이터"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("메시지")
                        )))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("성공"))
                .andExpect(jsonPath("$.data.isClicked").value(true))
                .andExpect(status().isOk());
    }

}
