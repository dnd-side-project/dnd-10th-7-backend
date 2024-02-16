package com.sendback.domain.feedback.fixture;

import com.sendback.domain.feedback.dto.request.SaveFeedbackRequestDto;
import com.sendback.domain.feedback.dto.response.FeedbackDetailResponseDto;
import com.sendback.domain.feedback.dto.response.SubmitFeedbackResponseDto;
import com.sendback.domain.feedback.entity.Feedback;
import com.sendback.domain.feedback.entity.FeedbackSubmit;
import com.sendback.domain.project.entity.Project;
import com.sendback.domain.user.entity.User;
import com.sendback.global.common.constants.Level;

import java.time.LocalDate;

import static com.sendback.domain.project.entity.Progress.PLANNING;
import static com.sendback.global.common.constants.FieldName.ART;

public class FeedbackFixture {

    private static final String TITLE = "title";
    private static final String LINK_URL = "linkUrl";
    private static final String CONTENT = "content";
    private static final String REWARD_MESSAGE = "rewardMessage";
    private static final LocalDate STARTED_AT = LocalDate.of(2024, 1, 1);
    private static final LocalDate ENDED_AT = LocalDate.of(2024, 1, 5);
    private static final String SCREEN_SHOT_URL = "screenShotUrl";

    public static final SaveFeedbackRequestDto MOCK_SAVE_FEEDBACK_REQUEST = new SaveFeedbackRequestDto(
            TITLE, LINK_URL, CONTENT, REWARD_MESSAGE, STARTED_AT, ENDED_AT);

    public static final FeedbackDetailResponseDto MOCK_FEEDBACK_DETAIL_RESPONSE = new FeedbackDetailResponseDto(
            1L, "유저 닉네임", Level.ONE.getName(), "프로필 이미지 url", 1L, "피드백 제목", "피드백 링크",
            "피드백 내용", "추가 리워드 메시지", "yyyy-MM-dd HH:mm", LocalDate.of(2024, 1, 12).toString(),
            LocalDate.of(2024, 1, 15).toString(),1L, "프로젝트 ID", ART.getName(), PLANNING.getValue());

    public static final SubmitFeedbackResponseDto MOCK_SUBMIT_FEEDBACK_RESPONSE = new SubmitFeedbackResponseDto(
            Level.ONE.getName(), false, 4L);

    public static FeedbackSubmit createDummyFeedbackSubmit(User user, Feedback feedback) {
        return FeedbackSubmit.of(user, feedback, SCREEN_SHOT_URL);
    }

    public static Feedback createDummyFeedback(User user, Project project) {
        return Feedback.of(user, project, MOCK_SAVE_FEEDBACK_REQUEST);
    }

}