package com.sendback.domain.field.service;

import com.sendback.domain.field.entity.Field;
import com.sendback.domain.field.repository.FieldRepository;
import com.sendback.global.exception.type.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import static com.sendback.domain.field.exception.FieldExceptionType.NOT_FOUND_FIELD;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FieldService {

    private final FieldRepository fieldRepository;

    public Field getFieldByName(String name) {
        return fieldRepository.findByName(name).orElseThrow(() -> new NotFoundException(NOT_FOUND_FIELD));
    }
    @Transactional
    public List<Field> saveAll(List<Field> fieldList) {
        return fieldRepository.saveAll(fieldList);
    }
}
