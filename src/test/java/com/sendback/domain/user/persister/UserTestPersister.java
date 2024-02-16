package com.sendback.domain.user.persister;

import com.sendback.domain.user.entity.*;
import com.sendback.domain.user.repository.UserRepository;
import com.sendback.global.Persister;
import lombok.RequiredArgsConstructor;

import static org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils.random;

@RequiredArgsConstructor
@Persister
public class UserTestPersister {

    private final UserRepository userRepository;

    public UserBuilder builder() {
        return new UserBuilder();
    }

    public final class UserBuilder {


        private SocialType socialType;
        private String socialId;
        private String email;
        private String socialName;
        private Level level;
        private Gender gender;
        private String birthDay;
        private String profileImageUrl;
        private Career career;
        private String nickname;

        public UserBuilder socialType(SocialType socialType) {
            this.socialType = socialType;
            return this;
        }

        public UserBuilder socialId(String socialId) {
            this.socialId = socialId;
            return this;
        }

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder socialName(String socialName) {
            this.socialName = socialName;
            return this;
        }

        public UserBuilder level(Level level) {
            this.level = level;
            return this;
        }

        public UserBuilder gender(Gender gender) {
            this.gender = gender;
            return this;
        }

        public UserBuilder birthDay(String birthDay) {
            this.birthDay = birthDay;
            return this;
        }

        public UserBuilder profileImageUrl(String profileImageUrl) {
            this.profileImageUrl = profileImageUrl;
            return this;
        }

        public UserBuilder career(Career career) {
            this.career = career;
            return this;
        }

        public UserBuilder nickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public User save() {
            User user = User.of(
                    (socialType == null ? SocialType.KAKAO : socialType),
                    (socialId == null ? random(10, true, true) : socialId),
                    (email == null ? "test@email.com" : email),
                    (level == null ? Level.ONE : level),
                    (socialName == null ? random(10, true, true) : socialName),
                    (gender == null ? Gender.MALE : gender),
                    (birthDay == null ? "20000112" : birthDay),
                    (profileImageUrl == null ? "profileImageUrl" : profileImageUrl),
                    (career == null ? Career.BACKEND : career),
                    (nickname == null ? random(10, true, true) : nickname)
            );
            return userRepository.save(user);
        }
    }


}
