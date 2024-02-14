package com.sendback.domain.user.entity;

import com.sendback.global.exception.type.NotFoundException;
import lombok.Getter;

import static com.sendback.domain.project.exception.ProjectExceptionType.NOT_FOUND_PROGRESS;
import static com.sendback.domain.user.exception.UserExceptionType.NOT_FOUND_GENDER;

@Getter
public enum Gender {
    MALE, FEMALE;
    public static Gender toEnum(String gender) {
        return switch (gender.toUpperCase()) {
            case "MALE" -> MALE;
            case "FEMALE" -> FEMALE;


            default -> throw new NotFoundException(NOT_FOUND_GENDER);
        };
    }
}
