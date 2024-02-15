package com.sendback.domain.field.fixture;

import com.sendback.domain.field.entity.Field;
import com.sendback.domain.user.entity.User;
import com.sendback.global.common.constants.FieldName;

import java.util.List;

public class FieldFixture {

    public static List<Field> createMockFields(User user) {
        return List.of(Field.of(FieldName.IT, user), Field.of(FieldName.EDU, user), Field.of(FieldName.ENVIRONMENT, user));
    }
}
