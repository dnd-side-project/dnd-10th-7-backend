package com.sendback.global.dummy;

import com.sendback.domain.field.entity.Field;
import com.sendback.domain.field.repository.FieldRepository;
import com.sendback.domain.user.entity.User;
import com.sendback.domain.user.repository.UserRepository;
import com.sendback.global.common.constants.FieldName;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.sendback.global.common.constants.FieldName.*;

@Component("fieldDummy")
@DependsOn("userDummy")
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FieldDummy {

    private final UserRepository userRepository;
    private final FieldRepository fieldRepository;

    @PostConstruct
    public void init() {
        if (fieldRepository.count() > 0) {
            log.info("[fieldDummy] 좋아요 데이터가 이미 존재");
        } else {
            createFields();
            log.info("[fieldDummy] 좋아요 더미 생성 완료");
        }
    }

    private void createFields() {

        List<User> users = userRepository.findAll();

        List<FieldName> fieldNames = List.of(EDU, FINANCE, ENVIRONMENT, GAME, IT, ART, HEALTH, HOBBY);

        for (User user : users) {
            Field field = Field.of(
                    fieldNames.get((int) (Math.random() * 100) % fieldNames.size()),
                    user
            );

            fieldRepository.save(field);
        }

    }


}
