package com.sendback.global.config.image;

import com.sendback.global.exception.type.ImageException;
import lombok.AllArgsConstructor;

import static com.sendback.global.config.image.exception.ImageExceptionType.NOT_FOUND_IMAGE_PATH;

@AllArgsConstructor
public enum S3SaveDir {

    USER("/user/profile"),
    PROJECT("/project");

    public final String path;

    public static S3SaveDir toEnum(String stringParam) {
        return switch (stringParam.toLowerCase()) {
            case "user" -> USER;
            case "project" -> PROJECT;

            default -> throw new ImageException(NOT_FOUND_IMAGE_PATH);
        };
    }
}