package com.sendback.domain.project.fixture;

import com.sendback.domain.field.entity.Field;
import com.sendback.domain.project.dto.request.SaveProjectRequest;
import com.sendback.domain.project.entity.Progress;
import com.sendback.domain.project.entity.Project;
import com.sendback.domain.user.entity.User;

import java.time.LocalDate;

public class ProjectFixture {

    private static final String TITLE = "title";
    private static final String FIELD = "edu";
    private static final String CONTENT = "content";
    private static final String DEMO_SITE_URL = "demoUrl";
    private static final LocalDate START_DATE = LocalDate.of(2024, 1, 1);
    private static final LocalDate END_DATE = LocalDate.of(2024, 1, 3);
    private static final Progress PROGRESS = Progress.PLANNING;

    private static final SaveProjectRequest saveProjectRequest = new SaveProjectRequest(TITLE, FIELD, CONTENT, DEMO_SITE_URL, START_DATE,
            END_DATE, PROGRESS.toString(), 1L,2L,3L,4L);

    public static Project createDummyProject(User user) {
        return Project.of(user, Field.of(saveProjectRequest.field()), saveProjectRequest);
    }

}
