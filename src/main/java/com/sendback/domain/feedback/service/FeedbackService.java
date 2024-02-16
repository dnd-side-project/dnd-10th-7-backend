package com.sendback.domain.feedback.service;

import com.sendback.domain.feedback.dto.request.SaveFeedbackRequestDto;
import com.sendback.domain.feedback.dto.response.FeedbackDetailResponseDto;
import com.sendback.domain.feedback.dto.response.FeedbackIdResponseDto;
import com.sendback.domain.feedback.dto.response.SubmitFeedbackResponseDto;
import com.sendback.domain.feedback.entity.Feedback;
import com.sendback.domain.feedback.entity.FeedbackSubmit;
import com.sendback.domain.feedback.repository.FeedbackRepository;
import com.sendback.domain.feedback.repository.FeedbackSubmitRepository;
import com.sendback.domain.project.entity.Project;
import com.sendback.domain.project.service.ProjectService;
import com.sendback.domain.user.entity.User;
import com.sendback.domain.user.service.UserService;
import com.sendback.domain.user.entity.Level;
import com.sendback.global.config.image.service.ImageService;
import com.sendback.global.exception.type.BadRequestException;
import com.sendback.global.exception.type.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static com.sendback.domain.feedback.exception.FeedbackExceptionType.DUPLICATE_FEEDBACK_SUBMIT;
import static com.sendback.domain.feedback.exception.FeedbackExceptionType.NOT_FOUND_FEEDBACK;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FeedbackService {

    private final UserService userService;
    private final ProjectService projectService;
    private final ImageService imageService;
    private final FeedbackRepository feedbackRepository;
    private final FeedbackSubmitRepository feedbackSubmitRepository;

    @Transactional
    public FeedbackIdResponseDto saveFeedback(Long userId, Long projectId, SaveFeedbackRequestDto saveFeedbackRequestDto) {
        User loginUser = userService.getUserById(userId);
        Project project = projectService.getProjectById(projectId);

        projectService.validateProjectAuthor(loginUser, project);

        Feedback feedback = Feedback.of(loginUser, project, saveFeedbackRequestDto);
        Long responseId = feedbackRepository.save(feedback).getId();

        return new FeedbackIdResponseDto(responseId);
    }

    public FeedbackDetailResponseDto getFeedbackDetail(Long projectId, Long feedbackId) {
        projectService.getProjectById(projectId);
        Feedback feedback = getFeedback(feedbackId);

        return FeedbackDetailResponseDto.from(feedback);
    }

    public Feedback getFeedback(Long feedbackId) {
        return feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_FEEDBACK));
    }

    @Transactional
    public SubmitFeedbackResponseDto submitFeedback(Long userId, Long feedbackId, MultipartFile file) {
        User loginUser = userService.getUserById(userId);
        Feedback feedback = getFeedback(feedbackId);

        validateAlreadySubmit(loginUser, feedback);

        String screenShotUrl = imageService.uploadImage(file, "feedback");
        FeedbackSubmit feedbackSubmit = FeedbackSubmit.of(loginUser, feedback, screenShotUrl);
        feedbackSubmitRepository.save(feedbackSubmit);

        Long submitCount = feedbackSubmitRepository.countByUserAndIsDeletedIsFalse(loginUser);
        boolean isLevelUp = isLevelUpUserLevelBySubmitting(loginUser, submitCount);
        Long remainCount = Level.getRemainCountUntilNextLevel(submitCount);

        return new SubmitFeedbackResponseDto(loginUser.getLevel().getName(), isLevelUp, remainCount);
    }

    private boolean isLevelUpUserLevelBySubmitting(User user, Long submitCount) {
        Level level = Level.getLevelByFeedbackSubmitCount(submitCount);
        if (user.getLevel().equals(level))
            return false;

        user.levelUp(level);
        return true;
    }

    private void validateAlreadySubmit(User loginUser, Feedback feedback) {
        boolean exists = feedbackSubmitRepository.existsByUserAndFeedbackAndIsDeletedIsFalse(loginUser, feedback);
        if (exists)
            throw new BadRequestException(DUPLICATE_FEEDBACK_SUBMIT);
    }

}
