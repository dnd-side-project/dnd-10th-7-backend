package com.sendback.domain.project.persister;

import com.sendback.domain.project.entity.Project;
import com.sendback.domain.project.entity.ProjectImage;
import com.sendback.domain.project.repository.ProjectImageRepository;
import com.sendback.global.Persister;
import lombok.RequiredArgsConstructor;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

@RequiredArgsConstructor
@Persister
public class ProjectImageTestPersister {

    private final ProjectImageRepository projectImageRepository;
    private final ProjectTestPersister projectTestPersister;

    private Project project;
    private String imageUrl;

    public ProjectImageTestPersister project(Project project) {
        this.project = project;
        return this;
    }

    public ProjectImageTestPersister imageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public ProjectImage save() {
        ProjectImage projectImage = ProjectImage.of(
                (project == null ? projectTestPersister.save() : project),
                (imageUrl == null ? RandomStringUtils.random(10, true, true) : imageUrl)
        );
        return projectImageRepository.save(projectImage);
    }
}
