package com.sendback.domain.project.service;

import com.sendback.domain.project.entity.Project;
import com.sendback.domain.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectSchedulerService {

    private final ProjectRepository projectRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void updateProjectByEndDate() {

        LocalDate now = LocalDate.now();
        List<Project> projects = projectRepository.findAllByEndedAtBeforeAndIsDeletedIsFalse(now);

        projects.forEach(
                Project::updateIsFinished);
    }
}
