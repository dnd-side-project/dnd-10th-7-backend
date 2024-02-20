package com.sendback.global.dummy;

import com.sendback.domain.project.entity.Project;
import com.sendback.domain.project.repository.ProjectRepository;
import com.sendback.domain.scrap.entity.Scrap;
import com.sendback.domain.scrap.repository.ScrapRepository;
import com.sendback.domain.user.entity.User;
import com.sendback.domain.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component("scrapDummy")
@DependsOn("projectDummy")
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ScrapDummy {

    private final ScrapRepository scrapRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    @PostConstruct
    public void init() {
        if (scrapRepository.count() > 0) {
            log.info("[scrapDummy] 스크랩 데이터가 이미 존재");
        } else {
            createScraps();
            log.info("[scrapDummy] 스크랩 더미 생성 완료");
        }
    }

    private void createScraps() {
        List<User> users = userRepository.findAll();

        List<Project> projects = projectRepository.findAll();

        List<Boolean> choice = List.of(Boolean.TRUE, Boolean.FALSE);

        users.forEach(
                user -> projects.stream().filter(project -> choice.get((int) (Math.random() * 100) % choice.size()).equals(Boolean.TRUE))
                        .map(project -> Scrap.of(user, project)).forEach(scrapRepository::save));
    }
}
