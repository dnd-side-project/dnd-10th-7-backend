package com.sendback.domain.field.service;

import com.sendback.domain.field.entity.Field;
import com.sendback.domain.field.repository.FieldRepository;
import com.sendback.global.exception.type.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.sendback.domain.field.exception.FieldExceptionType.NOT_FOUND_FIELD;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
public class FieldServiceTest {

    @InjectMocks
    FieldService fieldService;

    @Mock
    FieldRepository fieldRepository;

    @Nested
    @DisplayName("분야를 조회 시")
    class GetField {

        @Test
        @DisplayName("정상적인 요청이라면 분야를 조회한다.")
        public void success() throws Exception {
            //given
            Field art = Field.of("art");
            given(fieldRepository.findByName("art")).willReturn(Optional.of(art));

            //when
            Field response = fieldService.getFieldByName("art");

            //then
            assertThat(response).usingRecursiveComparison().isEqualTo(art);
        }

        @Test
        @DisplayName("존재하지 않는다면 예외를 발생한다.")
        public void fail_notExist() throws Exception {
            //given
            given(fieldRepository.findByName("예외")).willReturn(Optional.empty());

            //when - then
            assertThatThrownBy(() -> fieldService.getFieldByName("예외"))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessage(NOT_FOUND_FIELD.getMessage());
        }
    }
}