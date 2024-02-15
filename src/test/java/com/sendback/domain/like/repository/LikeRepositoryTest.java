package com.sendback.domain.like.repository;

import com.sendback.domain.like.entity.Like;
import com.sendback.domain.project.entity.Project;
import com.sendback.domain.user.entity.User;
import com.sendback.global.RepositoryTest;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class LikeRepositoryTest extends RepositoryTest {

    @Autowired
    LikeRepository likeRepository;

    @Nested
    @DisplayName("User와 Project로 Like를 조회할 때")
    class findByUserAndProject{

        @Test
        @DisplayName("값이 없으면 Optional 반환한다.")
        public void optional() throws Exception {
            //given
            User user = userTestPersister.save();
            Project project = projectTestPersister.save();

            //when
            Optional<Like> response = likeRepository.findByUserAndProject(user, project);

            //then
            assertThat(response).isEmpty();
        }

        @Test
        @DisplayName("값이 존재하면 값을 반환한다.")
        public void present() throws Exception {
            //given
            Like like = likeTestPersister.save();

            //when
            Optional<Like> response = likeRepository.findByUserAndProject(like.getUser(), like.getProject());

            //then
            assertThat(response).isPresent();
        }
    }

    @Test
    @DisplayName("특정 프로젝트 리스트들이 받은 좋아요 개수를 리턴한다.")
    public void countByProjects() {
        // given
        User user = userTestPersister.save();
        Project project = projectTestPersister.user(user).save();

        List<Project> projectList = new ArrayList<>();
        projectList.add(project);
        Like like = likeTestPersister.user(user).project(project).save();

        // when
        Long feedBackCount = likeRepository.countByProjects(projectList);

        // then
        assertThat(feedBackCount).isEqualTo(1);
    }

}
