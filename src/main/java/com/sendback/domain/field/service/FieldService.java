package com.sendback.domain.field.service;

import com.sendback.domain.field.entity.Field;
import com.sendback.domain.field.repository.FieldRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FieldService {

    private final FieldRepository fieldRepository;

    @Transactional
    public List<Field> saveAll(List<Field> fieldList) {
        return fieldRepository.saveAll(fieldList);
    }
}
