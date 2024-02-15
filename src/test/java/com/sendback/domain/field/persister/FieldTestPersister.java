package com.sendback.domain.field.persister;

import com.sendback.domain.field.entity.Field;
import com.sendback.domain.field.repository.FieldRepository;
import com.sendback.domain.user.entity.User;
import com.sendback.domain.user.persister.UserTestPersister;
import com.sendback.global.Persister;
import com.sendback.global.common.constants.FieldName;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.sendback.global.common.constants.FieldName.*;
import static com.sendback.global.common.constants.FieldName.EDU;
import static com.sendback.global.common.constants.FieldName.ENVIRONMENT;

@RequiredArgsConstructor
@Persister
public class FieldTestPersister {

    private final UserTestPersister userTestPersister;
    private final FieldRepository fieldRepository;

    private FieldName fieldName;
    private User user;

    public FieldTestPersister fieldName(FieldName fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    public FieldTestPersister user(User user) {
        this.user = user;
        return this;
    }

    private final List<FieldName> fieldNames
            = List.of(ART, FINANCE, ENVIRONMENT, EDU, HEALTH, IT, HOBBY, GAME);

    public Field save() {
        Field field = Field.of(
                (fieldName == null ? fieldNames.get((int) (Math.random() * 100) % fieldNames.size()) : fieldName),
                (user == null ? userTestPersister.save() : user));
        return fieldRepository.save(field);
    }
}