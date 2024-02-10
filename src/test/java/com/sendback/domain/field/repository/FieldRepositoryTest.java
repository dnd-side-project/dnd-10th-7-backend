package com.sendback.domain.field.repository;

import com.sendback.domain.field.entity.Field;
import com.sendback.global.annotation.SendbackJpaTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@SendbackJpaTest
@TestInstance(PER_CLASS)
public class FieldRepositoryTest {

    @Autowired
    FieldRepository fieldRepository;

    @BeforeAll
    public void init() {
        List<Field> fields = fieldNames.stream().map(Field::of).toList();
        fieldRepository.saveAll(fields);
    }

    List<String> fieldNames = List.of("art", "finance", "environment", "edu", "health", "IT", "hobby", "game", "etc");

    @Nested
    @DisplayName("name으로 field 조회 시")
    class FindByName {

        @Test
        @DisplayName("정상적인 요청이라면 값을 반환한다.")
        public void success() throws Exception {
            //given
            String name = "art";

            //when
            Optional<Field> artFieldOptional = fieldRepository.findByName(name);

            //then
            assertThat(artFieldOptional).isPresent();
        }

        @Test
        @DisplayName("존재하지 않으면 반환하지 않는다.")
        public void fail_notExist() throws Exception {
            //given
            String name = "무작위";

            //when
            Optional<Field> fieldOptional = fieldRepository.findByName(name);

            //then
            assertThat(fieldOptional).isEmpty();
        }
    }



}
