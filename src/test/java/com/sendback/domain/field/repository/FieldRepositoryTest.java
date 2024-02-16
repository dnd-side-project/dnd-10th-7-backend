package com.sendback.domain.field.repository;

import com.sendback.domain.field.entity.Field;
import com.sendback.global.RepositoryTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FieldRepositoryTest extends RepositoryTest {

    @Autowired
    FieldRepository fieldRepository;

    @Test
    @DisplayName("특정 uerId를 가지는 field들을 반환한다.")
    public void findAllByUserId() {

        //given
        Field field = fieldTestPersister.builder().save();

        // when
        List<Field> fieldList = fieldRepository.findAllByUserId(field.getUser().getId());

        // then
        assertThat(fieldList.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("특정 uerId를 가지는 field들을 제거한다.")
    public void deleteByUserId() {

        //given
        Field field = fieldTestPersister.builder().save();

        // when
        Long deletedUserCount = fieldRepository.deleteByUserId(field.getUser().getId());

        // then
        assertThat(deletedUserCount).isEqualTo(1);
    }
}
