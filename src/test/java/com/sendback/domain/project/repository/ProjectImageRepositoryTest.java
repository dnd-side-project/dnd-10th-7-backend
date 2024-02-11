package com.sendback.domain.project.repository;

import com.sendback.domain.project.entity.Project;
import com.sendback.domain.project.entity.ProjectImage;
import com.sendback.global.RepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class ProjectImageRepositoryTest extends RepositoryTest {

    @Autowired
    ProjectImageRepository projectImageRepository;

    @Nested
    @DisplayName("프로젝트와 이미지 url로 프로젝트 이미지를 조회할 때")
    class findByProjectAndImageUrl {
        @Test
        @DisplayName("잘못된 프로젝트와 이미지 url로 image 테이블을 가져오면 반환 값이 없다.")
        public void optional() throws Exception {
            //given
            Project project = projectTestPersister.save();

            //when
            Optional<ProjectImage> response = projectImageRepository.findByProjectAndImageUrl(project, "notFound");

            //then
            assertThat(response).isEmpty();
        }

        @Test
        @DisplayName("정상 요청시 값을 반환한다.")
        public void findByProjectAndImageUrl_success() throws Exception {
            //given
            Project project = projectTestPersister.save();
            ProjectImage projectImage = projectImageTestPersister.project(project).save();

            //when
            Optional<ProjectImage> response = projectImageRepository.findByProjectAndImageUrl(projectImage.getProject(), projectImage.getImageUrl());

            //then
            assertThat(response).isPresent();
        }
    }

    @Nested
    @DisplayName("프로젝트로 프로젝트 이미지를 전체 조회를 할 때")
    class findAllByProject {
        @Test
        @DisplayName("이미지가 없을 시 빈값 반환")
        public void emptyList() throws Exception {
            //given
            Project project = projectTestPersister.save();

            //when
            List<ProjectImage> response = projectImageRepository.findAllByProject(project);

            //then
            assertThat(response).isEmpty();
        }

        @Test
        @DisplayName("이미지가 있을 시 정상으로 값을 반환한다.")
        public void success() throws Exception {
            //given
            Project project = projectTestPersister.save();
            projectImageTestPersister.project(project);
            projectImageTestPersister.save();
            projectImageTestPersister.save();
            projectImageTestPersister.save();

            Project anotherProject = projectTestPersister.save();
            projectImageTestPersister.project(anotherProject);
            projectImageTestPersister.save(); // 다른 프로젝트

            //when
            List<ProjectImage> response = projectImageRepository.findAllByProject(project);

            //then
            assertThat(response.size()).isEqualTo(3);
        }
    }
}
