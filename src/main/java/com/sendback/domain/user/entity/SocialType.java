package com.sendback.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import com.sendback.global.exception.type.NotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.sendback.domain.project.exception.ProjectExceptionType.NOT_FOUND_PROGRESS;
import static com.sendback.domain.user.exception.UserExceptionType.NOT_FOUND_SOCIAL_TYPE;

@Getter
public enum SocialType {
    KAKAO, GOOGLE;
    public static SocialType toEnum(String socialType) {
        return switch (socialType.toUpperCase()) {
            case "KAKAO" -> KAKAO;
            case "GOOGLE" -> GOOGLE;


            default -> throw new NotFoundException(NOT_FOUND_SOCIAL_TYPE);
        };
    }
}
