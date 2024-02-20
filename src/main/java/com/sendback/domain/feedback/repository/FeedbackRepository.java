package com.sendback.domain.feedback.repository;

import com.sendback.domain.feedback.entity.Feedback;
import com.sendback.domain.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    List<Feedback> findTop3ByProjectAndIsDeletedIsFalseOrderByIdDesc(Project project);
    List<Feedback> findAllByEndedAtBeforeAndIsDeletedIsFalse(LocalDate localDate);

}
