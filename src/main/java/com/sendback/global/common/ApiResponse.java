package com.sendback.global.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiResponse<T> {
    private Integer code;
    private T data;
    private String message;

    private ApiResponse(T data) {
        this.code = 200;
        this.data = data;
        this.message = "성공";
    }

    private ApiResponse(int status, T data, String message) {
        this.code = status;
        this.data = data;
        this.message = message;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(data);
    }

}
