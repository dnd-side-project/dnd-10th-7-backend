package com.sendback.domain.user.repository;

import com.sendback.domain.project.entity.Project;
import com.sendback.domain.project.entity.ProjectImage;
import com.sendback.domain.user.entity.User;
import com.sendback.global.RepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static com.sendback.domain.user.fixture.UserFixture.createDummyUser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserRepositoryTest extends RepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Nested
    @DisplayName("닉네임을 통한 유저 조회")
    class findByNickname {
        @Test
        @DisplayName("db에 존재하는 유저를 닉네임을 통해 조회한다. ")
        public void findByNickname_success() {
            //given
            User user = createDummyUser();
            userRepository.save(user);

            //when
            Optional<User> findUser = userRepository.findByNickname(user.getNickname());

            //then
            assertTrue(findUser.isPresent());
            assertEquals(user.getId(), findUser.get().getId());
            assertEquals(user.getNickname(), findUser.get().getNickname());
        }

        @Test
        @DisplayName("db에 찾고자하는 유저의 닉네임이 없으면 null 반환한다. ")
        public void findByNickname_null() {
            //given
            User user = createDummyUser();

            //when
            Optional<User> findUser = userRepository.findByNickname(user.getNickname());

            //then
            assertTrue(!findUser.isPresent());
        }
    }

}
