package com.sendback.domain.project.entity;

import com.sendback.global.exception.type.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.sendback.domain.project.exception.ProjectExceptionType.NOT_FOUND_PROGRESS;

@Getter
@AllArgsConstructor
public enum Progress {
    PLANNING, DEVELOPING, REFACTORING;

    public static Progress toEnum(String progress) {
        return switch (progress.toUpperCase()) {
            case "PLANNING" -> PLANNING;
            case "DEVELOPING" -> DEVELOPING;
            case "REFACTORING" -> REFACTORING;

            default -> throw new NotFoundException(NOT_FOUND_PROGRESS);
        };
    }
}
