package com.sendback.domain.user.fixture;

import com.sendback.domain.user.dto.SigningAccount;
import com.sendback.domain.user.dto.request.SignUpRequestDto;
import com.sendback.domain.user.entity.User;
import com.sendback.domain.user.entity.SocialType;

import com.sendback.domain.user.entity.*;
import java.util.Arrays;
import static com.sendback.domain.user.entity.Career.BACKEND;
import static com.sendback.domain.user.entity.Gender.MALE;
import static com.sendback.domain.user.entity.Level.ONE;

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
    private static final Level LEVEL = ONE;
    private static final String SOCIALNAME = "테스트";
    private static final Gender GENDER = MALE;
    private static final String BIRTHDAY = "2000.01.01";
    private static final Career CAREER = BACKEND;
    public static final SigningAccount MOCK_SIGNING_ACCOUNT = new SigningAccount("123", "mock",
            "mock_profile", "mock@kakao.com", "kakao");
    public static final SignUpRequestDto MOCK_SIGN_UP_REQUEST_DTO = new SignUpRequestDto("mock_user", "2000.01.01",
            "남자", "백엔드", Arrays.asList("예술/대중문화", "게임"), "valid signToken");

    public static final SignUpRequestDto MOCK_INVALID_SIGN_TOKEN_SIGN_UP_REQUEST_DTO = new SignUpRequestDto("mock_user", "2000.01.01",
            "남자", "백엔드", Arrays.asList("예술/대중문화", "게임"), "Invalid signToken");

    public static final User mock_user = User.of(MOCK_SIGNING_ACCOUNT, MOCK_SIGN_UP_REQUEST_DTO);

    public static User createDummyUser() {
        return User.of(MOCK_SIGNING_ACCOUNT, MOCK_SIGN_UP_REQUEST_DTO);
    }

    public static User createDummyUser_B() {
        return User.of(SOCIAL_TYPE_KAKAO, SOCIAL_ID_B, EMAIL_B, NICKNAME_B, PROFILE_IMAGE_URL_B);
    }

    public static User createDummyUser_C() {
        return User.of(SOCIAL_TYPE_KAKAO, SOCIAL_ID_B, EMAIL_B, LEVEL, SOCIALNAME, GENDER, BIRTHDAY,
                PROFILE_IMAGE_URL, CAREER, NICKNAME_B);
    }
    public static User createDummyUser_D() {
        return User.of(1L, SOCIAL_TYPE_KAKAO, SOCIAL_ID_B, EMAIL_B, LEVEL, SOCIALNAME, GENDER, BIRTHDAY,
                PROFILE_IMAGE_URL, CAREER, NICKNAME_B);
    }
    public static User createDummyUser_E() {
        return User.of(2L, SOCIAL_TYPE_KAKAO, SOCIAL_ID_B, EMAIL_B, LEVEL, SOCIALNAME, GENDER, BIRTHDAY,
                PROFILE_IMAGE_URL, CAREER, NICKNAME_B);
    }

}