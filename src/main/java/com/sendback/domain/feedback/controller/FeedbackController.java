package com.sendback.domain.feedback.controller;

import com.sendback.domain.feedback.dto.request.SaveFeedbackRequestDto;
import com.sendback.domain.feedback.dto.response.FeedbackDetailResponseDto;
import com.sendback.domain.feedback.dto.response.FeedbackIdResponseDto;
import com.sendback.domain.feedback.dto.response.SubmitFeedbackResponseDto;
import com.sendback.domain.feedback.service.FeedbackService;
import com.sendback.global.common.ApiResponse;
import com.sendback.global.common.UserId;
import jakarta.validation.Valid;
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
    public ApiResponse<FeedbackIdResponseDto> saveFeedback(
            @UserId Long userId,
            @PathVariable Long projectId,
            @RequestBody @Valid SaveFeedbackRequestDto saveFeedbackRequestDto) {
        return success(feedbackService.saveFeedback(userId, projectId, saveFeedbackRequestDto));
    }

    @GetMapping("/{projectId}/feedbacks/{feedbackId}")
    public ApiResponse<FeedbackDetailResponseDto> getFeedbackDetail(
            @PathVariable Long projectId,
            @PathVariable Long feedbackId) {
        return success(feedbackService.getFeedbackDetail(projectId, feedbackId));
    }

    @PostMapping(value = "/{projectId}/feedbacks/{feedbackId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<SubmitFeedbackResponseDto> submitFeedback(
            @UserId Long userId,
            @PathVariable Long projectId,
            @PathVariable Long feedbackId,
            @ModelAttribute MultipartFile file) {
        return success(feedbackService.submitFeedback(userId, feedbackId, file));
    }
}
