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

    public ProjectImageBuilder builder() {
        return new ProjectImageBuilder();
    }

    public final class ProjectImageBuilder {

        private Project project;
        private String imageUrl;

        public ProjectImageBuilder project(Project project) {
            this.project = project;
            return this;
        }

        public ProjectImageBuilder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public ProjectImage save() {
            ProjectImage projectImage = ProjectImage.of(
                    (project == null ? projectTestPersister.builder().save() : project),
                    (imageUrl == null ? RandomStringUtils.random(10, true, true) : imageUrl)
            );
            return projectImageRepository.save(projectImage);
        }

    }

}
