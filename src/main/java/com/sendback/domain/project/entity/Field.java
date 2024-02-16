package com.sendback.domain.project.entity;

import com.sendback.global.exception.type.NotFoundException;
import lombok.AllArgsConstructor;

import static com.sendback.domain.project.exception.ProjectExceptionType.NOT_FOUND_FIELD;

@AllArgsConstructor
public enum Field {
    ART("예술/대중문화"),
    ENVIRONMENT("환경"),
    HEALTH("건강"),
    HOBBY("취미/실용"),
    FINTECH("금융/핀테크"),
    EDUCATION("교육"),
    GAME("게임"),
    AI("AI/머신러닝");

    private final String value;

    public static Field toEnum(String field) {
        for (Field value : Field.values()) {
            if (value.value.equalsIgnoreCase(field)) {
                return value;
            }
        }
        throw new NotFoundException(NOT_FOUND_FIELD);
    }

    public String toString() {
        return value;
    }
}