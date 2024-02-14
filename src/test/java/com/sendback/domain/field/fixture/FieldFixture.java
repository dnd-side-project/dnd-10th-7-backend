package com.sendback.domain.field.fixture;

import com.sendback.domain.field.entity.Field;

import java.util.Arrays;
import java.util.List;

public class FieldFixture {

    public static final List<Field> mock_Fields = Arrays.asList(
            Field.of("art"), Field.of("game"), Field.of("IT")
    );
}
