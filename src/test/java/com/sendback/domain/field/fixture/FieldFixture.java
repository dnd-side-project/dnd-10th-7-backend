package com.sendback.domain.field.fixture;

import com.sendback.domain.field.entity.Field;
import com.sendback.domain.project.dto.request.SaveProjectRequest;

import java.util.Arrays;
import java.util.List;

public class FieldFixture {

    public static final List<Field> mock_inputFields = Arrays.asList(
            Field.of("art"), Field.of("game"), Field.of("IT")
    );

    public static final List<Field> mock_outputFields = Arrays.asList(
            Field.of("art"), Field.of("game"), Field.of("IT")
    );
}
