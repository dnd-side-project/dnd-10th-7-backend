package com.sendback.domain.user.entity;

import com.sendback.domain.project.entity.Progress;
import com.sendback.global.exception.type.NotFoundException;

import static com.sendback.domain.project.exception.ProjectExceptionType.NOT_FOUND_PROGRESS;
import static com.sendback.domain.user.exception.UserExceptionType.NOT_FOUND_CAREER;

public enum Career {
    DESIGNER, PLANNER,FRONTEND, BACKEND;

    public static Career toEnum(String career) {
        return switch (career.toUpperCase()) {
            case "DESIGNER" -> DESIGNER;
            case "PLANNER" -> PLANNER;
            case "FRONTEND" -> FRONTEND;
            case "BACKEND" -> BACKEND;

            default -> throw new NotFoundException(NOT_FOUND_CAREER);
        };
    }
}
