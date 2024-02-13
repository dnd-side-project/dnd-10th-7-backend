package com.sendback.domain.like.repository;

import com.sendback.domain.like.entity.Like;
import com.sendback.domain.project.entity.Project;
import com.sendback.domain.user.entity.User;
import com.sendback.global.RepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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

}
