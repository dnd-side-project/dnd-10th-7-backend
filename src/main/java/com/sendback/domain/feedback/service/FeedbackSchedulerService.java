package com.sendback.domain.feedback.service;

import com.sendback.domain.feedback.entity.Feedback;
import com.sendback.domain.feedback.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class FeedbackSchedulerService {

    private final FeedbackRepository feedbackRepository;

    @Scheduled(cron = "15 0 0 * * *")
    public void updateProjectByEndDate() {

        LocalDate now = LocalDate.now();
        List<Feedback> feedbacks = feedbackRepository.findAllByEndedAtBeforeAndIsDeletedIsFalse(now);

        feedbacks.forEach(
                Feedback::updateIsFinished);
    }

}
