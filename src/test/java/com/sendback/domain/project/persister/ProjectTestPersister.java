package com.sendback.domain.project.persister;

import com.sendback.domain.field.entity.Field;
import com.sendback.domain.field.persister.FieldTestPersister;
import com.sendback.domain.project.dto.request.SaveProjectRequest;
import com.sendback.domain.project.entity.Progress;
import com.sendback.domain.project.entity.Project;
import com.sendback.domain.project.repository.ProjectRepository;
import com.sendback.domain.user.entity.User;
import com.sendback.domain.user.persister.UserTestPersister;
import com.sendback.global.Persister;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@RequiredArgsConstructor
@Persister
public class ProjectTestPersister {

    private final ProjectRepository projectRepository;
    private final UserTestPersister userTestPersister;
    private final FieldTestPersister fieldTestPersister;

    private User user;
    private Field field;
    private SaveProjectRequest saveProjectRequest;

    public ProjectTestPersister user(User user) {
        this.user = user;
        return this;
    }

    public ProjectTestPersister field(Field field) {
        this.field = field;
        return this;
    }

    public ProjectTestPersister saveProjectRequest(SaveProjectRequest saveProjectRequest) {
        this.saveProjectRequest = saveProjectRequest;
        return this;
    }

    private static final String TITLE = "title";
    private static final String FIELD = "edu";
    private static final String CONTENT = "content";
    private static final String SUMMARY = "summary";
    private static final String DEMO_SITE_URL = "demoUrl";
    private static final LocalDate START_DATE = LocalDate.of(2024, 1, 1);
    private static final LocalDate END_DATE = LocalDate.of(2024, 1, 3);
    private static final Progress PLANNING_PROGRESS = Progress.PLANNING;

    public Project save() {
        Project project = Project.of(
                (user == null ? userTestPersister.save() : user),
                (field == null ? fieldTestPersister.save() : field),
                (saveProjectRequest == null ? new SaveProjectRequest(TITLE, FIELD, CONTENT, SUMMARY, DEMO_SITE_URL, START_DATE, END_DATE,
                        PLANNING_PROGRESS.toString(), 1L, 2L, 3L, 4L) : saveProjectRequest)
        );
        return projectRepository.save(project);
    }

}
