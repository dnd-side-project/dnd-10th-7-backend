package com.sendback.domain.feedback.repository;

import com.sendback.domain.feedback.entity.Feedback;
import com.sendback.domain.feedback.entity.FeedbackSubmit;
import com.sendback.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FeedbackSubmitRepository extends JpaRepository<FeedbackSubmit, Long> {

    boolean existsByUserAndFeedbackAndIsDeletedIsFalse(User user, Feedback feedback);
    Long countByUserAndIsDeletedIsFalse(User user);

    @Query("Select COUNT(s) from FeedbackSubmit s where s.user.id = :userId")
    Long countByUserId(@Param("userId") Long userId);

}
