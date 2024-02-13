package com.sendback.domain.user.fixture;

import com.sendback.domain.user.entity.User;
import com.sendback.domain.user.entity.SocialType;

public class UserFixture {

    private static final SocialType SOCIAL_TYPE_KAKAO = SocialType.KAKAO;
    private static final String SOCIAL_ID = "1";
    private static final String SOCIAL_ID_B = "2";
    private static final String EMAIL = "test@test.com";
    private static final String EMAIL_B = "testB@test.com";
    private static final String NICKNAME = "테스트";
    private static final String NICKNAME_B = "테스트B";
    private static final String PROFILE_IMAGE_URL = "테스트 이미지";
    private static final String PROFILE_IMAGE_URL_B = "테스트 이미지_B";

    public static User createDummyUser() {
        return User.of(SOCIAL_TYPE_KAKAO, SOCIAL_ID, EMAIL, NICKNAME, PROFILE_IMAGE_URL);
    }

    public static User createDummyUser_B() {
        return User.of(SOCIAL_TYPE_KAKAO, SOCIAL_ID_B, EMAIL_B, NICKNAME_B, PROFILE_IMAGE_URL_B);
    }
}
