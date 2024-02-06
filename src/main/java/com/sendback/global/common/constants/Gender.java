package com.sendback.global.common.constants;

import lombok.Getter;

@Getter
public enum Gender {
    MALE("MALE"), FEMALE("FEMALE");
    private final String gender;

    Gender(String gender){
        this.gender = gender;
    }
}
