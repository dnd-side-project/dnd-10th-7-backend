package com.sendback.domain.user.exception;

import com.sendback.global.exception.ExceptionType;
import lombok.Getter;

@Getter
public enum UserExceptionType implements ExceptionType {

    INVALID_SIGN_TOKEN(2000, "유효하지 않은 sign token 입니다."),
    NOT_FOUND_CAREER(2010, "유효하지 않은 career 형식입니다."),
    NOT_FOUND_GENDER(2020, "유효하지 않은 gender 형식입니다."),
    NOT_FOUND_SOCIAL_TYPE(2030, "유효하지 않은 socail type 형식입니다."),
    NOT_FOUND_LEVEL(2040, "유효하지 않은 level 형식입니다."),
    INVALID_NICKNAME(2050, "닉네임은 한글, 영어(2~8자) 이내로 입력해주세요."),
    NOT_FOUND_USER(2060, "유저를 찾을 수 없습니다."),
    DUPLICATED_NICKNAME(2070, "중복된 유저 닉네임입니다."),
    PREVIOUS_REGISTERED_USER(2080, "이전에 회원 가입한 내역이 있습니다.");
    private final int statusCode;
    private final String message;

    UserExceptionType(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
