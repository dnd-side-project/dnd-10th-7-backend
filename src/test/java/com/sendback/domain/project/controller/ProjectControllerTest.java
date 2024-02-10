//package com.sendback.domain.project.controller;
//
//import com.sendback.domain.project.dto.request.SaveProjectRequest;
//import com.sendback.domain.project.service.ProjectService;
//import com.sendback.global.exception.type.ImageException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.BDDMockito;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//import org.springframework.web.filter.CharacterEncodingFilter;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.imageio.ImageIO;
//import java.awt.image.BufferedImage;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.time.LocalDate;
//import java.util.List;
//
//import static com.sendback.global.config.image.exception.ImageExceptionType.AWS_S3_UPLOAD_FAIL;
//import static org.mockito.BDDMockito.given;
//import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//
//@WebMvcTest(controllers = ProjectController.class)
//public class ProjectControllerTest {
//
//    MockMvc mockMvc;
//    @MockBean
//    ProjectService projectService;
//
//    @BeforeEach
//    public void setup(final WebApplicationContext context) {
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
//                .addFilter(new CharacterEncodingFilter("UTF-8", true))
//                .alwaysDo(print())
//                .build();
//    }
//
//    @Test
//    @DisplayName("정상적인 요청이면 프로젝트를 등록할 때 성공을 반환한다.")
//    public void saveProject_success() throws Exception {
//        //given
//        Long responseId = 1L;
//        SaveProjectRequest saveProjectRequest = new SaveProjectRequest("title", "field", "content", "demoSiteUrl",
//                LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 3),
//                "planning", 1L, 2L, 3L, 4L);
//        given(projectService.saveProject(1L, saveProjectRequest, List.of(mockingMultipartFile("sendback.jpg")))).willReturn(responseId);
//
//        //when
//        mockMvc.perform(post("/projects"))
//                .andExpect()
//        //then
//
//    }
//
//    private MultipartFile mockingMultipartFile(String fileName) {
//        return new MockMultipartFile(
//                "images",
//                fileName,
//                MediaType.IMAGE_JPEG_VALUE,
//                generateMockImage()
//        );
//    }
//
//    private byte[] generateMockImage() {
//        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
//
//        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
//            ImageIO.write(image, "jpg", byteArrayOutputStream);
//            return byteArrayOutputStream.toByteArray();
//        } catch (IOException e) {
//            throw new ImageException(AWS_S3_UPLOAD_FAIL);
//        }
//    }
//}
