package com.sendback.domain.user.entity;

import com.sendback.global.exception.type.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.sendback.domain.user.exception.UserExceptionType.NOT_FOUND_CAREER;

@Getter
@AllArgsConstructor
public enum Career {
    DESIGNER("디자이너"), PLANNER("기획자"),FRONTEND("프론트엔드"), BACKEND("백엔드");

    private final String value;

    public static Career toEnum(String career) {
        return switch (career) {
            case "디자이너" -> DESIGNER;
            case "기획자" -> PLANNER;
            case "프론트엔드" -> FRONTEND;
            case "백엔드" -> BACKEND;

            default -> throw new NotFoundException(NOT_FOUND_CAREER);
        };
    }
}
