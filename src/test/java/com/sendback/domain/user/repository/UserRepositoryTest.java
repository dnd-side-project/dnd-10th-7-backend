package com.sendback.domain.user.repository;

import com.sendback.domain.user.entity.User;
import com.sendback.domain.user.persister.UserTestPersister;
import com.sendback.global.RepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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
            User user = userTestPersister.builder().save();

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
            //given when
            Optional<User> findUser = userRepository.findByNickname("test name");

            //then
            assertTrue(!findUser.isPresent());
        }
    }

    @Nested
    @DisplayName("pullUpCnt가 1 이상인 유저 조회")
    class findAllByPullUpCntIsGreaterThan {

        @Test
        @DisplayName("검색에 성공한다.")
        public void success_existUsers() throws Exception {
            //given
            UserTestPersister.UserBuilder builder = userTestPersister.builder();
            User user_A = builder.save();
            User user_B = builder.save();
            user_B.actPullUp();
            User user_C = builder.save();
            user_C.actPullUp();
            User user_D = builder.save();

            //when
            List<User> users = userRepository.findAllByPullUpCntIsGreaterThan(0L);

            //then
            assertThat(users.size()).isEqualTo(2);
            assertThat(users).containsExactlyInAnyOrder(user_B, user_C);

        }
    }

}
