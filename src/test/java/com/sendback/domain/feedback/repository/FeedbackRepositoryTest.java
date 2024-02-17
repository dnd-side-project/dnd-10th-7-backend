package com.sendback.domain.feedback.repository;

import com.sendback.domain.feedback.entity.Feedback;
import com.sendback.domain.feedback.persister.FeedbackTestPersister;
import com.sendback.global.RepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FeedbackRepositoryTest extends RepositoryTest {

    @Autowired
    FeedbackRepository feedbackRepository;

    @Nested
    @DisplayName("feedback 테이블에서 삭제되지 않은 가장 최신의 피드백 3개 이하를 조회하는 경우")
    class findTop3ByProjectAndIsDeletedIsFalseOrderByIdDesc {

        @Test
        @DisplayName("한 개인 경우 한 개만 반환한다.")
        public void success_oneFeedback() throws Exception {
            //given
            Feedback feedback = feedbackTestPersister.builder().save();

            //when
            List<Feedback> response = feedbackRepository.findTop3ByProjectAndIsDeletedIsFalseOrderByIdDesc(feedback.getProject());

            //then
            assertThat(response.size()).isEqualTo(1);

        }

        @Test
        @DisplayName("두 개인 경우 두 개만 반환한다.")
        public void success_twoFeedback() throws Exception {
            //given
            FeedbackTestPersister.FeedbackBuilder feedbackBuilder = feedbackTestPersister.builder();
            Feedback feedback_A = feedbackBuilder.save();
            feedbackBuilder.project(feedback_A.getProject()).save();

            //when
            List<Feedback> response = feedbackRepository.findTop3ByProjectAndIsDeletedIsFalseOrderByIdDesc(feedback_A.getProject());

            //then
            assertThat(response.size()).isEqualTo(2);

        }

        @Test
        @DisplayName("세 개 이상인 경우 최신 세 개만 반환한다.")
        public void success_threeFeedback() throws Exception {
            //given
            FeedbackTestPersister.FeedbackBuilder feedbackBuilder = feedbackTestPersister.builder();
            Feedback feedback_A = feedbackBuilder.save();
            Feedback feedback_third = feedbackBuilder.project(feedback_A.getProject()).save();
            Feedback feedback_second = feedbackBuilder.project(feedback_A.getProject()).save();
            Feedback feedback_first = feedbackBuilder.project(feedback_A.getProject()).save();

            //when
            List<Feedback> response = feedbackRepository.findTop3ByProjectAndIsDeletedIsFalseOrderByIdDesc(feedback_A.getProject());

            //then
            assertThat(response.size()).isEqualTo(3);
            assertThat(response.get(0)).usingRecursiveComparison().isEqualTo(feedback_first);
            assertThat(response.get(1)).usingRecursiveComparison().isEqualTo(feedback_second);
            assertThat(response.get(2)).usingRecursiveComparison().isEqualTo(feedback_third);
        }
    }
}
