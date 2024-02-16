package com.sendback.domain.feedback.persister;

import com.sendback.domain.feedback.entity.Feedback;
import com.sendback.domain.feedback.entity.FeedbackSubmit;
import com.sendback.domain.feedback.repository.FeedbackSubmitRepository;
import com.sendback.domain.user.entity.User;
import com.sendback.domain.user.persister.UserTestPersister;
import com.sendback.global.Persister;
import lombok.RequiredArgsConstructor;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

@RequiredArgsConstructor
@Persister
public class FeedbackSubmitTestPersister {

    private final UserTestPersister userTestPersister;
    private final FeedbackTestPersister feedbackTestPersister;
    private final FeedbackSubmitRepository feedbackSubmitRepository;

    public FeedbackSubmitBuilder builder() {
        return new FeedbackSubmitBuilder();
    }

    public final class FeedbackSubmitBuilder {

        private User user;
        private Feedback feedback;
        private String screenShotUrl;

        public FeedbackSubmitBuilder user(User user) {
            this.user = user;
            return this;
        }

        public FeedbackSubmitBuilder feedback(Feedback feedback) {
            this.feedback = feedback;
            return this;
        }

        public FeedbackSubmitBuilder screenShotUrl(String screenShotUrl) {
            this.screenShotUrl = screenShotUrl;
            return this;
        }

        public FeedbackSubmit save() {
            FeedbackSubmit feedbackSubmit = FeedbackSubmit.of(
                    (user == null ? userTestPersister.builder().save() : user),
                    (feedback == null ? feedbackTestPersister.builder().save() : feedback),
                    (screenShotUrl == null ? RandomStringUtils.random(10, true, true) : screenShotUrl));
            return feedbackSubmitRepository.save(feedbackSubmit);
        }

    }


}
