package com.sendback.domain.project.persister;

import com.sendback.domain.project.dto.request.SaveProjectRequestDto;
import com.sendback.domain.project.entity.Project;
import com.sendback.domain.project.repository.ProjectRepository;
import com.sendback.domain.user.entity.User;
import com.sendback.domain.project.entity.Progress;
import com.sendback.domain.user.persister.UserTestPersister;
import com.sendback.global.Persister;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

import static com.sendback.global.common.constants.FieldName.IT;

@RequiredArgsConstructor
@Persister
public class ProjectTestPersister {

    private final ProjectRepository projectRepository;
    private final UserTestPersister userTestPersister;

    private User user;
    private SaveProjectRequestDto saveProjectRequestDto;

    public ProjectTestPersister user(User user) {
        this.user = user;
        return this;
    }

    public ProjectTestPersister saveProjectRequestDto(SaveProjectRequestDto saveProjectRequestDto) {
        this.saveProjectRequestDto = saveProjectRequestDto;
        return this;
    }

    private static final String TITLE = "title";
    private static final String CONTENT = "content";
    private static final String SUMMARY = "summary";
    private static final String DEMO_SITE_URL = "demoUrl";
    private static final LocalDate START_DATE = LocalDate.of(2024, 1, 1);
    private static final LocalDate END_DATE = LocalDate.of(2024, 1, 3);
    private static final Progress PLANNING_PROGRESS = Progress.PLANNING;

    public Project save() {
        Project project = Project.of(
                (user == null ? userTestPersister.save() : user),
                (saveProjectRequestDto == null ? new SaveProjectRequestDto(TITLE, IT.getName(), CONTENT, SUMMARY, DEMO_SITE_URL, START_DATE, END_DATE,
                        PLANNING_PROGRESS.getValue(), 1L, 2L, 3L, 4L) : saveProjectRequestDto)
        );
        return projectRepository.save(project);
    }

}
