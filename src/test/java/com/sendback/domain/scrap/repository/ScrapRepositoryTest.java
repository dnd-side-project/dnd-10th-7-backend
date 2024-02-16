package com.sendback.domain.scrap.repository;

import com.sendback.domain.project.entity.Project;
import com.sendback.domain.scrap.entity.Scrap;
import com.sendback.domain.user.entity.User;
import com.sendback.global.RepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class ScrapRepositoryTest extends RepositoryTest {

    @Autowired
    ScrapRepository scrapRepository;

    @Nested
    @DisplayName("User와 Project로 Scrap을 조회할 때")
    class findByUserAndProject{

        @Test
        @DisplayName("값이 없으면 Optional 반환한다.")
        public void optional() throws Exception {
            //given
            Project project = projectTestPersister.builder().save();

            //when
            Optional<Scrap> response = scrapRepository.findByUserAndProject(project.getUser(), project);

            //then
            assertThat(response).isEmpty();
        }

        @Test
        @DisplayName("값이 존재하면 값을 반환한다.")
        public void present() throws Exception {
            //given
            Scrap scrap = scrapTestPersister.builder().save();

            //when
            Optional<Scrap> response = scrapRepository.findByUserAndProject(scrap.getUser(), scrap.getProject());

            //then
            assertThat(response).isPresent();
        }
    }

    @Nested
    @DisplayName("프로젝트에 스크랩 여부를 조회할 때")
    class existsByUserAndProjectAndIsDeletedIsFalse {

        @Test
        @DisplayName("없으면 false를 반환한다.")
        public void success_false() throws Exception {
            //given
            User user = userTestPersister.builder().save();
            Project project = projectTestPersister.builder().save();

            //when
            boolean exists = scrapRepository.existsByUserAndProjectAndIsDeletedIsFalse(user, project);

            //then
            assertThat(exists).isFalse();
        }

        @Test
        @DisplayName("있으면 true를 반환한다.")
        public void success_true() throws Exception {
            //given
            Scrap scrap = scrapTestPersister.builder().save();

            //when
            boolean exists = scrapRepository.existsByUserAndProjectAndIsDeletedIsFalse(scrap.getUser(), scrap.getProject());

            //then
            assertThat(exists).isTrue();
        }

    }


}
