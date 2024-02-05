package com.sendback.global.common.constants;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public enum SocialType {
    KAKAO("KAKAO"), GOOGLE("GOOGLE");
    private final String socialType;

    SocialType(String socialType){
        this.socialType = socialType;
    }
}
