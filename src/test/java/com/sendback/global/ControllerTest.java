package com.sendback.global;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sendback.domain.auth.controller.AuthController;
import com.sendback.domain.auth.service.AuthService;
import com.sendback.domain.auth.service.GoogleService;
import com.sendback.domain.auth.service.KakaoService;
import com.sendback.domain.comment.controller.CommentController;
import com.sendback.domain.comment.service.CommentService;
import com.sendback.domain.feedback.controller.FeedbackController;
import com.sendback.domain.feedback.service.FeedbackService;
import com.sendback.domain.like.controller.LikeController;
import com.sendback.domain.like.service.LikeService;
import com.sendback.domain.project.controller.ProjectController;
import com.sendback.domain.project.service.ProjectService;
import com.sendback.domain.scrap.controller.ScrapController;
import com.sendback.domain.scrap.service.ScrapService;

import com.sendback.domain.user.controller.UserController;
import com.sendback.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;


@WebMvcTest({
        ProjectController.class,
        LikeController.class,
        ScrapController.class,
        AuthController.class,
        FeedbackController.class,
        UserController.class,
        CommentController.class
})
@ActiveProfiles("test")
@AutoConfigureRestDocs
public abstract class ControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected ProjectService projectService;

    @MockBean
    protected LikeService likeService;

    @MockBean
    protected ScrapService scrapService;

    @MockBean
    protected KakaoService kakaoService;

    @MockBean
    protected GoogleService googleService;

    @MockBean
    protected AuthService authService;

    @MockBean
    protected FeedbackService feedbackService;

    @MockBean
    protected UserService userService;

    @MockBean
    protected CommentService commentService;

    protected static final String ACCESS_TOKEN_PREFIX = "Bearer ";

    protected MockMultipartFile mockingMultipartFile(String fileName) {
        return new MockMultipartFile(
                "images",
                fileName,
                MediaType.IMAGE_JPEG_VALUE,
                "mock image".getBytes()
        );
    }

    protected OperationRequestPreprocessor customRequestPreprocessor() {
        return preprocessRequest(
                modifyHeaders()
                        .remove("X-CSRF-TOKEN") //csrf 헤더 삭제하기
                        .remove("Host"),
                prettyPrint());
    }
}
