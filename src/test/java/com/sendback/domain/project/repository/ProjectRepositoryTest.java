package com.sendback.domain.project.repository;

import com.sendback.domain.project.entity.Project;
import com.sendback.global.RepositoryTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ProjectRepositoryTest extends RepositoryTest {

    @Autowired
    ProjectRepository projectRepository;

    @Test
    @DisplayName("특정 userId를 외래키로 가지는 프로젝트의 개수를 반환한다.")
    public void countByUserId() {
        //given
        Project project = projectTestPersister.builder().save();

        //when
        Long projectCount = projectRepository.countByUserId(project.getUser().getId());

        //then
        assertThat(projectCount).isEqualTo(1);
    }

    @Test
    @DisplayName("특정 uerId를 가지는 프로젝트들을 반환한다.")
    public void findByUserId() {
        //given
        Project project = projectTestPersister.builder().save();

        //when
        List<Project> findProject = projectRepository.findByUserId(project.getUser().getId());

        //then
        assertThat(findProject.size()).isEqualTo(1);
    }
}
