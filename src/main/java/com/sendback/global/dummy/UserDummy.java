package com.sendback.global.dummy;

import com.sendback.domain.user.entity.*;
import com.sendback.domain.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.sendback.domain.user.entity.Career.*;
import static com.sendback.domain.user.entity.Career.FRONTEND;
import static com.sendback.domain.user.entity.Gender.*;
import static com.sendback.domain.user.entity.Gender.FEMALE;
import static com.sendback.domain.user.entity.Level.*;

@Component("userDummy")
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserDummy {

    private final UserRepository userRepository;

    @PostConstruct
    public void init() {

        if (userRepository.count() > 0) {
            log.info("[userDummy] 더미 유저가 이미 존재합니다.");
        } else {
            createUsers();
            log.info("[userDummy] 더미 유저 생성 완료");
        }
    }
    private void createUsers() {

        List<com.sendback.domain.user.entity.Level> levels = List.of(ONE, TWO, THREE, FOUR, FIVE);

        List<Gender> genders = List.of(MALE, FEMALE);

        List<String> profileImageUrl = List.of("https://chillin-bucket.s3.ap-northeast-2.amazonaws.com/project/sad_bbdf9fb8-a6c4-4215-8901-bff7dc773824.png",
                "https://chillin-bucket.s3.ap-northeast-2.amazonaws.com/project/read_90478a3a-11cf-439d-809a-efbe8226e078.png",
                "https://chillin-bucket.s3.ap-northeast-2.amazonaws.com/project/movie_3caafd63-4ac6-4f73-9e57-fbead2f24d52.png",
                "https://chillin-bucket.s3.ap-northeast-2.amazonaws.com/project/meal_a68f61d3-8aef-49fd-8cc6-53390f26d793.png");

        List<Career> careers = List.of(FRONTEND, BACKEND, DESIGNER, PLANNER);

        for (int cnt = 0; cnt < 30; cnt++) {

            User user = User.of(
                    SocialType.KAKAO,
                    "test " + cnt,
                    "test " + cnt + "@kakao.com",
                    levels.get((int) (Math.random() * 100) % levels.size()),
                    "testSocialName " + cnt,
                    genders.get((int) (Math.random() * 100) % genders.size()),
                    "2000.01.12",
                    profileImageUrl.get((int) (Math.random() * 100) % profileImageUrl.size()),
                    careers.get((int) (Math.random() * 100) % careers.size()),
                    "testNickname " + cnt
            );

            userRepository.save(user);
        }
    }
}
