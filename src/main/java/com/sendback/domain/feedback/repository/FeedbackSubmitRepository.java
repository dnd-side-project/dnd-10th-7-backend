package com.sendback.domain.feedback.repository;

import com.sendback.domain.feedback.entity.Feedback;
import com.sendback.domain.feedback.entity.FeedbackSubmit;
import com.sendback.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackSubmitRepository extends JpaRepository<FeedbackSubmit, Long> {

    boolean existsByUserAndFeedbackAndIsDeletedIsFalse(User user, Feedback feedback);
    Long countByUserAndIsDeletedIsFalse(User user);

}
