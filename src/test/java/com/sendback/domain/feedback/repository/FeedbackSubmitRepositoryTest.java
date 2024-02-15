package com.sendback.domain.feedback.repository;

import com.sendback.domain.feedback.entity.FeedbackSubmit;
import com.sendback.domain.project.entity.Project;
import com.sendback.domain.user.entity.User;
import com.sendback.global.RepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class FeedbackSubmitRepositoryTest extends RepositoryTest {

    @Autowired
    FeedbackSubmitRepository feedbackSubmitRepository;

    @Nested
    @DisplayName("user와 feedback으로 삭제되지 않은 피드백 제출이 있는 지 조회했을 때")
    class existsByUserAndFeedbackAndIsDeletedIsFalse {

        @Test
        @DisplayName("값이 있으면 true를 반환한다.")
        public void success_true() throws Exception {
            //given
            FeedbackSubmit feedbackSubmit = feedbackSubmitTestPersister.save();

            //when
            boolean exists = feedbackSubmitRepository.existsByUserAndFeedbackAndIsDeletedIsFalse(feedbackSubmit.getUser(), feedbackSubmit.getFeedback());

            //then
            assertThat(exists).isTrue();

        }

        @Test
        @DisplayName("삭제된 값이 있으면 false를 반환한다.")
        public void success_false() throws Exception {
            //given
            FeedbackSubmit feedbackSubmit = feedbackSubmitTestPersister.save();
            feedbackSubmitRepository.delete(feedbackSubmit);

            //when
            boolean exists = feedbackSubmitRepository.existsByUserAndFeedbackAndIsDeletedIsFalse(feedbackSubmit.getUser(), feedbackSubmit.getFeedback());

            //then
            assertThat(exists).isFalse();

        }
    }

    @Test
    @DisplayName("특정 uerId를 가지는 피드백들을 반환한다.")
    public void countByUserId() {
        // given
        User user = userTestPersister.save();
        feedbackSubmitTestPersister.user(user).save();

        // when
        Long feedBackCount = feedbackSubmitRepository.countByUserId(user.getId());

        // then
        assertThat(feedBackCount).isEqualTo(1);
    }

}
