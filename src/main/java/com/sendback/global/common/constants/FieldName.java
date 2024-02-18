package com.sendback.global.common.constants;

import com.sendback.global.exception.type.BadRequestException;
import com.sendback.global.exception.type.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.sendback.domain.field.exception.FieldExceptionType.NOT_FOUND_FIELD;

@Getter
@AllArgsConstructor
public enum FieldName {

    ART("예술/대중문화"),
    FINANCE("금융/핀테크"),
    ENVIRONMENT("환경"),
    EDU("교육"),
    HEALTH("건강"),
    IT("AI/머신러닝"),
    HOBBY("취미/실용"),
    GAME("게임");

    private final String name;

    public static FieldName toEnum(String fieldName) {
        return switch (fieldName) {
            case "예술/대중문화" -> ART;
            case "금융/핀테크" -> FINANCE;
            case "환경" -> ENVIRONMENT;
            case "교육" -> EDU;
            case "건강" -> HEALTH;
            case "AI/머신러닝" -> IT;
            case "취미/실용" -> HOBBY;
            case "게임" -> GAME;

            default -> throw new BadRequestException(NOT_FOUND_FIELD);
        };
    }

    public static String toKorean(String fieldName) {
        return switch (fieldName) {
            case "ART" -> "예술/대중문화";
            case "FINANCE" -> "금융/핀테크";
            case "ENVIRONMENT" -> "환경";
            case "EDU" -> "교육";
            case "HEALTH" -> "건강";
            case "IT" -> "AI/머신러닝";
            case "HOBBY" -> "취미/실용";
            case "GAME" -> "게임";

            default -> throw new NotFoundException(NOT_FOUND_FIELD);
        };


    }

}
