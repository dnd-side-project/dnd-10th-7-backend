package com.sendback.domain.field.entity;

import com.sendback.domain.user.entity.User;
import com.sendback.global.common.constants.FieldName;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Field {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private FieldName name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    private Field(FieldName name, User user) {
        this.name = name;
        this.user = user;
    }

    public static Field of(FieldName name, User user) {
        return Field.builder()
                .name(name)
                .user(user)
                .build();
    }
}
