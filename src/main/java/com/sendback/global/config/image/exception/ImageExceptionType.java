package com.sendback.global.config.image.exception;

import com.sendback.global.exception.ExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ImageExceptionType implements ExceptionType {

    NOT_FOUND_IMAGE_PATH(10000, "이미지 저장 경로가 올바르지 않습니다."),
    AWS_S3_UPLOAD_FAIL(10010, "S3 이미지 업로드에 실패했습니다."),
    AWS_S3_DELETE_FAIL(10020, "S3 이미지 삭제에 실패했습니다.");

    private final int statusCode;
    private final String message;

}
