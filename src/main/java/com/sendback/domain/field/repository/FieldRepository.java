package com.sendback.domain.field.repository;

import com.sendback.domain.field.entity.Field;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FieldRepository extends JpaRepository<Field, Long> {

    Optional<Field> findByName(String name);
}
