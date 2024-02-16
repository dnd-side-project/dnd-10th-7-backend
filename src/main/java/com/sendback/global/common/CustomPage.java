package com.sendback.global.common;

import lombok.*;
import org.springframework.data.domain.Page;
import java.util.List;

@Builder
@Getter
public class CustomPage<T> {

    int page;
    int size;
    Long totalElements;
    int totalPages;
    List<T> content;

    public static <T> CustomPage<T> of(Page<T> page) {
        return CustomPage.<T>builder()
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .content(page.getContent())
                .build();
    }
}