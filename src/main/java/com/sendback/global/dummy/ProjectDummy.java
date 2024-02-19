package com.sendback.global.dummy;

import com.sendback.domain.project.dto.request.SaveProjectRequestDto;
import com.sendback.domain.project.entity.Progress;
import com.sendback.domain.project.entity.Project;
import com.sendback.domain.project.repository.ProjectRepository;
import com.sendback.domain.user.entity.User;
import com.sendback.domain.user.repository.UserRepository;
import com.sendback.global.common.constants.FieldName;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.sendback.global.common.constants.FieldName.*;

@Component("projectDummy")
@DependsOn("userDummy")
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProjectDummy {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @PostConstruct
    public void init() {
        if (projectRepository.count() > 0) {
            log.info("[projectDummy] 프로젝트 데이터가 이미 존재");
        } else {
            createProjects();
            log.info("[projectDummy] 프로젝트 더미 생성 완료");
        }
    }

    private void createProjects() {
        List<User> users = userRepository.findAll();

        List<FieldName> fieldNames = List.of(EDU, FINANCE, ENVIRONMENT, GAME, IT, ART, HEALTH, ETC, HOBBY);

        List<Integer> startMonths = List.of(1, 2, 3 ,4 ,5 ,6);
        List<Integer> endMonths = List.of(7, 8, 9 ,10 ,11 ,12);
        List<Integer> startDays = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
        List<Integer> endDays = List.of(11, 12, 13, 14, 15, 16, 17, 18, 19);

        List<Progress> progresses = List.of(Progress.DEVELOPING, Progress.PLANNING, Progress.REFACTORING);

        List<Long> counts = List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L);
        int cnt = 1;

        for (User user : users) {

            Project project = Project.of(user,
                    new SaveProjectRequestDto(
                            "title " + cnt,
                            fieldNames.get((int) (Math.random() * 100) % fieldNames.size()).getName(),
                            "content " + cnt,
                            "summary " + cnt,
                            "demoSiteUrl " + cnt,
                            LocalDate.of(2023,
                                    startMonths.get((int) (Math.random() * 100) % startMonths.size()),
                                    startDays.get((int) (Math.random() * 100) % startDays.size())),
                            LocalDate.of(2023,
                                    endMonths.get((int) (Math.random() * 100) % endMonths.size()),
                                    endDays.get((int) (Math.random() * 100) % endDays.size())),
                            progresses.get((int) (Math.random() * 100) % progresses.size()).getValue(),
                            counts.get((int) (Math.random() * 100) % counts.size()),
                            counts.get((int) (Math.random() * 100) % counts.size()),
                            counts.get((int) (Math.random() * 100) % counts.size()),
                            counts.get((int) (Math.random() * 100) % counts.size())
                    ));

            cnt += 1;
            projectRepository.save(project);

        }

    }
}
