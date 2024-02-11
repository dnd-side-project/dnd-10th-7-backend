package com.sendback.domain.field.repository;

import com.sendback.domain.field.entity.Field;
import com.sendback.global.RepositoryTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class FieldRepositoryTest extends RepositoryTest {

    @Autowired
    FieldRepository fieldRepository;

    @Nested
    @DisplayName("name으로 field 조회 시")
    class FindByName {

        @Test
        @DisplayName("정상적인 요청이라면 값을 반환한다.")
        public void success() throws Exception {
            //given
            fieldTestPersister.saveAll();
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
            String name = "art";

            //when
            Optional<Field> fieldOptional = fieldRepository.findByName(name);

            //then
            assertThat(fieldOptional).isEmpty();
        }
    }
}
