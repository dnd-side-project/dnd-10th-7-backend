package com.sendback.domain.project.entity;

import com.sendback.global.exception.type.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.sendback.domain.project.exception.ProjectExceptionType.NOT_FOUND_PROGRESS;

@Getter
@AllArgsConstructor
public enum Progress {
    PLANNING("기획중"), DEVELOPING("개발중"), REFACTORING("리팩토링중");

    private final String value;

    public static Progress toEnum(String progress) {
        return switch (progress) {
            case "기획중" -> PLANNING;
            case "개발중" -> DEVELOPING;
            case "리팩토링중" -> REFACTORING;

            default -> throw new NotFoundException(NOT_FOUND_PROGRESS);
        };
    }

    public static String toKorean(String progress) {
        return switch (progress) {
            case "PLANNING" -> "기획중";
            case "DEVELOPING" -> "개발중";
            case "REFACTORING" -> "리팩토링중";

            default -> throw new NotFoundException(NOT_FOUND_PROGRESS);
        };
    }
}
