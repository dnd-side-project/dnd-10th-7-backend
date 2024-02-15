package com.sendback.domain.user.entity;

import com.sendback.global.exception.type.NotFoundException;

import static com.sendback.domain.user.exception.UserExceptionType.NOT_FOUND_CAREER;

public enum Career {
    DESIGNER("디자이너"),
    PLANNER("기획자"),
    FRONTEND("프론트엔드 개발자"),
    BACKEND("백엔드 개발자");

    private final String value;

    Career(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Career toEnum(String career) {
        return switch (career.toUpperCase()) {
            case "DESIGNER" -> DESIGNER;
            case "PLANNER" -> PLANNER;
            case "FRONTEND" -> FRONTEND;
            case "BACKEND" -> BACKEND;

            default -> throw new NotFoundException(NOT_FOUND_CAREER);
        };
    }

    public static String toString(Career career) {
        return switch (career) {
            case DESIGNER -> "디자이너";
            case PLANNER -> "기획자";
            case FRONTEND -> "프론트엔드 개발자";
            case BACKEND -> "백엔드 개발자";
        };
    }
}