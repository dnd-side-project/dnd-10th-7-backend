package com.sendback.global.dummy;

import com.sendback.domain.feedback.dto.request.SaveFeedbackRequestDto;
import com.sendback.domain.feedback.entity.Feedback;
import com.sendback.domain.feedback.repository.FeedbackRepository;
import com.sendback.domain.project.entity.Project;
import com.sendback.domain.project.repository.ProjectRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component("feedbackDummy")
@DependsOn("projectDummy")
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FeedbackDummy {

    private final FeedbackRepository feedbackRepository;
    private final ProjectRepository projectRepository;

    @PostConstruct
    public void init() {
        if (feedbackRepository.count() > 0) {
            log.info("[feedbackDummy] 피드백 데이터가 이미 존재");
        } else {
            createFeedbacks();
            log.info("[feedbackDummy] 피드백 더미 생성 완료");
        }
    }

    private void createFeedbacks() {

        List<Project> projects = projectRepository.findAll();

        List<Integer> counts = List.of(0, 1, 2, 3);

        List<Integer> startMonths = List.of(1, 2, 3 ,4 ,5 ,6);
        List<Integer> endMonths = List.of(7, 8, 9 ,10 ,11 ,12);
        List<Integer> startDays = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
        List<Integer> endDays = List.of(11, 12, 13, 14, 15, 16, 17, 18, 19);



        for (Project project : projects) {
            Integer countSize = counts.get((int) (Math.random() * 100) % counts.size());
            for (int cnt = 0; cnt < countSize; cnt ++) {
                Feedback feedback = Feedback.of(
                        project.getUser(),
                        project,
                        new SaveFeedbackRequestDto(
                                "title " + cnt,
                                "link " + cnt,
                                "content " + cnt,
                                "rewardMessage " + cnt,
                                LocalDate.of(2023,
                                        startMonths.get((int) (Math.random() * 100) % startMonths.size()),
                                        startDays.get((int) (Math.random() * 100) % startDays.size())),
                                LocalDate.of(2023,
                                        endMonths.get((int) (Math.random() * 100) % endMonths.size()),
                                        endDays.get((int) (Math.random() * 100) % endDays.size()))
                        )
                );
                feedbackRepository.save(feedback);
            }

        }
    }
}
