package com.sendback.domain.field.service;

import com.sendback.domain.field.entity.Field;
import com.sendback.domain.field.repository.FieldRepository;
import com.sendback.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;

import static com.sendback.domain.field.fixture.FieldFixture.createMockFields;
import static com.sendback.domain.user.fixture.UserFixture.mock_user;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;



@ExtendWith(MockitoExtension.class)
public class FieldServiceTest {

    @InjectMocks
    FieldService fieldService;

    @Mock
    FieldRepository fieldRepository;

    User user = mock_user;
    List<Field> fields = createMockFields(user);


    @Nested
    @DisplayName("분야를 일괄 저장 시")
    class saveAllField {


        @Test
        @DisplayName("성공하면 200을 반환한다.")
        void saveAllTest() {
            // given

            given(fieldRepository.saveAll(fields)).willReturn(fields);

            // when
            List<Field> result = fieldService.saveAll(fields);

            // then
            assertEquals(fields, result);
            verify(fieldRepository).saveAll(fields);
        }

    }

}
