package com.sendback.domain.field.persister;

import com.sendback.domain.field.entity.Field;
import com.sendback.domain.field.repository.FieldRepository;
import com.sendback.global.Persister;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Persister
public class FieldTestPersister {

    private final FieldRepository fieldRepository;

    private final List<String> fieldNames
            = List.of("art", "finance", "environment", "edu", "health", "IT", "hobby", "game", "etc");

    public void saveAll() {
        List<Field> fields = fieldNames.stream().map(Field::of).toList();
        fieldRepository.saveAll(fields);
    }

    public Field save() {
        Field field = Field.of(fieldNames.get((int) (Math.random() * 100) % fieldNames.size()));
        return fieldRepository.save(field);
    }
}
