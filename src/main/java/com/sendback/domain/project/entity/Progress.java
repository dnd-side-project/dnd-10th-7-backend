package com.sendback.domain.project.entity;

import com.sendback.global.exception.type.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.sendback.domain.project.exception.ProjectExceptionType.NOT_FOUND_PROGRESS;

@Getter
@AllArgsConstructor
public enum Progress {
    PLANNING("기획중"),
    DEVELOPING("개발중"),
    REFACTORING("리팩토링중");

    private final String value;

    public static Progress toEnum(String progress) {
        for (Progress value : Progress.values()) {
            if (value.value.equalsIgnoreCase(progress)) {
                return value;
            }
        }
        throw new NotFoundException(NOT_FOUND_PROGRESS);
    }

    public String toString() {
        return value;
    }
}