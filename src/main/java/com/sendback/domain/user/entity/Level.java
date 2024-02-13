package com.sendback.domain.user.entity;

import com.sendback.global.exception.type.NotFoundException;
import lombok.Getter;

import static com.sendback.domain.project.exception.ProjectExceptionType.NOT_FOUND_PROGRESS;
import static com.sendback.domain.user.exception.UserExceptionType.NOT_FOUND_LEVEL;

@Getter
public enum Level {
    ONE, TWO, THREE, FOUR, FIVE;
    public static Level toEnum(String level) {
        return switch (level.toUpperCase()) {
            case "ONE" -> ONE;
            case "TWO" -> TWO;
            case "THREE" -> THREE;
            case "FOUR" -> FOUR;
            case "FIVE" -> FIVE;

            default -> throw new NotFoundException(NOT_FOUND_LEVEL);
        };
    }
}
