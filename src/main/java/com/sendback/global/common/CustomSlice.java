package com.sendback.global.common;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Slice;

import java.util.List;

@Builder
@Getter
public class CustomSlice<T> {

    Boolean isLast;
    List<T> content;
    public static <T> CustomSlice<T> of(Slice<T> slice) {
        return CustomSlice.<T>builder()
                .isLast(slice.isLast())
                .content(slice.getContent())
                .build();
    }
}