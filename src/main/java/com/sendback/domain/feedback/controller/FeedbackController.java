package com.sendback.domain.feedback.controller;

import com.sendback.domain.feedback.dto.request.SaveFeedbackRequest;
import com.sendback.domain.feedback.dto.response.FeedbackDetailResponse;
import com.sendback.domain.feedback.dto.response.FeedbackIdResponse;
import com.sendback.domain.feedback.dto.response.SubmitFeedbackResponse;
import com.sendback.domain.feedback.service.FeedbackService;
import com.sendback.global.common.ApiResponse;
import com.sendback.global.common.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.sendback.global.common.ApiResponse.success;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/projects")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping("/{projectId}/feedback")
    public ApiResponse<FeedbackIdResponse> save(
            @UserId Long userId,
            @PathVariable Long projectId,
            @RequestBody SaveFeedbackRequest saveFeedbackRequest) {
        return success(feedbackService.saveFeedback(userId, projectId, saveFeedbackRequest));
    }

    @GetMapping("/{projectId}/feedbacks/{feedbackId}")
    public ApiResponse<FeedbackDetailResponse> getDetail(
            @PathVariable Long projectId,
            @PathVariable Long feedbackId) {
        return success(feedbackService.getFeedbackDetail(projectId, feedbackId));
    }

    @PostMapping(value = "/{projectId}/feedbacks/{feedbackId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<SubmitFeedbackResponse> submit(
            @UserId Long userId,
            @PathVariable Long projectId,
            @PathVariable Long feedbackId,
            @ModelAttribute MultipartFile file) {
        return success(feedbackService.submitFeedback(userId, feedbackId, file));
    }
}
