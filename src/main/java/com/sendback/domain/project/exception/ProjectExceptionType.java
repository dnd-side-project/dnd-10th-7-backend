package com.sendback.domain.project.exception;

import com.sendback.global.exception.ExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProjectExceptionType implements ExceptionType {

    NOT_FOUND_PROGRESS(4000, "진행 여부가 올바르지 않습니다."),
    NOT_FOUND_PROJECT(4010, "프로젝트 ID가 올바르지 않습니다."),
    NOT_PROJECT_AUTHOR(4020, "프로젝트 작성자가 아닙니다."),
    NOT_FOUND_DELETE_IMAGE_URL(4030, "삭제하려는 이미지 경로가 올바르지 않습니다."),
    DELETED_PROJECT(4040, "삭제된 프로젝트입니다."),
    IMAGE_SIZE_OVER(4050, "이미지는 최대 5장까지 가능합니다."),

    NOT_FOUND_FIELD(4060, "필드가 올바르지 않습니다.");

    private final int statusCode;
    private final String message;
}
