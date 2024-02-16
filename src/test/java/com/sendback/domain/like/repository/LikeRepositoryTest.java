package com.sendback.domain.like.repository;

import com.sendback.domain.like.entity.Like;
import com.sendback.domain.project.entity.Project;
import com.sendback.domain.user.entity.User;
import com.sendback.global.RepositoryTest;
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
            Project project = projectTestPersister.builder().save();

            //when
            Optional<Like> response = likeRepository.findByUserAndProject(project.getUser(), project);

            //then
            assertThat(response).isEmpty();
        }

        @Test
        @DisplayName("값이 존재하면 값을 반환한다.")
        public void present() throws Exception {
            //given
            Like like = likeTestPersister.builder().save();

            //when
            Optional<Like> response = likeRepository.findByUserAndProject(like.getUser(), like.getProject());

            //then
            assertThat(response).isPresent();
        }
    }

    @Nested
    @DisplayName("프로젝트에 좋아요 여부를 조회할 때")
    class existsByUserAndProjectAndIsDeletedIsFalse {

        @Test
        @DisplayName("없으면 false를 반환한다.")
        public void success_false() throws Exception {
            //given
            User user = userTestPersister.builder().save();
            Project project = projectTestPersister.builder().save();

            //when
            boolean exists = likeRepository.existsByUserAndProjectAndIsDeletedIsFalse(user, project);

            //then
            assertThat(exists).isFalse();
        }

        @Test
        @DisplayName("있으면 true를 반환한다.")
        public void success_true() throws Exception {
            //given
            Like like = likeTestPersister.builder().save();

            //when
            boolean exists = likeRepository.existsByUserAndProjectAndIsDeletedIsFalse(like.getUser(), like.getProject());

            //then
            assertThat(exists).isTrue();
        }

    }

    @Nested
    @DisplayName("특정 프로젝트 리스트들이 받은 좋아요 개수를 조회하는 경우")
    class countByProjects{
        @Test
        @DisplayName("조건을 만족하는 데이터 개수를 반환한다.")
        public void success() {
            // given
            User user = userTestPersister.builder().save();
            Project project = projectTestPersister.builder().user(user).save();

            List<Project> projectList = new ArrayList<>();
            projectList.add(project);
            Like like = likeTestPersister.builder().user(user).project(project).save();

            // when
            Long feedBackCount = likeRepository.countByProjectIn(projectList);

            // then
            assertThat(feedBackCount).isEqualTo(1);
        }
    }

}
