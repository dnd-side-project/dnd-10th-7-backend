package com.sendback.domain.feedback.persister;

import com.sendback.domain.feedback.dto.request.SaveFeedbackRequestDto;
import com.sendback.domain.feedback.entity.Feedback;
import com.sendback.domain.feedback.repository.FeedbackRepository;
import com.sendback.domain.project.entity.Project;
import com.sendback.domain.project.persister.ProjectTestPersister;
import com.sendback.domain.user.entity.User;
import com.sendback.domain.user.persister.UserTestPersister;
import com.sendback.global.Persister;
import lombok.RequiredArgsConstructor;

import static com.sendback.domain.feedback.fixture.FeedbackFixture.MOCK_SAVE_FEEDBACK_REQUEST;

@RequiredArgsConstructor
@Persister
public class FeedbackTestPersister {

    private final UserTestPersister userTestPersister;
    private final ProjectTestPersister projectTestPersister;
    private final FeedbackRepository feedbackRepository;

    private User user;
    private Project project;

    private SaveFeedbackRequestDto saveFeedbackRequestDto;

    public FeedbackTestPersister user(User user) {
        this.user = user;
        return this;
    }

    public FeedbackTestPersister project(Project project) {
        this.project = project;
        return this;
    }

    public FeedbackTestPersister saveFeedbackRequestDto(SaveFeedbackRequestDto saveFeedbackRequestDto) {
        this.saveFeedbackRequestDto = saveFeedbackRequestDto;
        return this;
    }

    public Feedback save() {
        Feedback feedback = Feedback.of(
                (user == null ? userTestPersister.save() : user),
                (project == null ? projectTestPersister.save() : project),
                (saveFeedbackRequestDto == null) ? MOCK_SAVE_FEEDBACK_REQUEST : saveFeedbackRequestDto
        );
        return feedbackRepository.save(feedback);
    }
}
