package com.sendback.domain.user.fixture;

import com.sendback.domain.user.dto.SigningAccount;
import com.sendback.domain.user.dto.request.SignUpRequestDto;
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
    public static final SigningAccount mock_signingAccount = new SigningAccount("123", "mock",
            "mock_profile", "mock@kakao.com", "kakao");
    public static final SignUpRequestDto mock_signUpRequestDto = new SignUpRequestDto("mock_user", "2000.01.01",
            "male", "backend", Arrays.asList("art", "game"), "valid signToken");

    public static final SignUpRequestDto mock_Invalid_SignToken_signUpRequestDto = new SignUpRequestDto("mock_user", "2000.01.01",
            "male", "backend", Arrays.asList("art", "game"), "Invalid signToken");

    public static final User mock_user = User.of(mock_signingAccount, mock_signUpRequestDto);

    public static User createDummyUser() {
        return User.of(SOCIAL_TYPE_KAKAO, SOCIAL_ID, EMAIL, NICKNAME, PROFILE_IMAGE_URL);
    }

    public static User createDummyUser_B() {
        return User.of(SOCIAL_TYPE_KAKAO, SOCIAL_ID_B, EMAIL_B, NICKNAME_B, PROFILE_IMAGE_URL_B);
    }

    public static User createDummyUser_C() {
        return User.of(SOCIAL_TYPE_KAKAO, SOCIAL_ID_B, EMAIL_B, LEVEL, SOCIALNAME, GENDER, BIRTHDAY,
                PROFILE_IMAGE_URL, CAREER, NICKNAME_B);
    }
}