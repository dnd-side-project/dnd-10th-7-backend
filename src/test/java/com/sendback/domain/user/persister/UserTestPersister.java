package com.sendback.domain.user.persister;

import com.sendback.domain.user.entity.User;
import com.sendback.domain.user.repository.UserRepository;
import com.sendback.global.Persister;
import com.sendback.global.common.constants.SocialType;
import lombok.RequiredArgsConstructor;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

@RequiredArgsConstructor
@Persister
public class UserTestPersister {

    private final UserRepository userRepository;

    private SocialType socialType;
    private String socialId;
    private String email = "test@test.com";
    private String nickname = "테스트";
    private String profileImageUrl = "테스트 이미지";

    public UserTestPersister socialType(SocialType socialType) {
        this.socialType = socialType;
        return this;
    }

    public UserTestPersister socialId(String socialId) {
        this.socialId = socialId;
        return this;
    }

    public UserTestPersister email(String email) {
        this.email = email;
        return this;
    }

    public UserTestPersister nickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public UserTestPersister profileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
        return this;
    }

    public User save() {
        User user = User.of(
                (socialType == null ? SocialType.KAKAO : socialType),
                (socialId == null ? "20000112" : socialId),
                (email == null ? "test@email.com" : email),
                (nickname == null ? RandomStringUtils.random(10, true, true) : nickname),
                (profileImageUrl == null ? "profileImageUrl" : profileImageUrl)
        );
        return userRepository.save(user);
    }
}
