package com.sendback.domain.field.entity;

import com.sendback.domain.user.entity.User;
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

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    private Field(String name, User user) {
        this.name = name;
        this.user = user;
    }

    public static Field of(String name) {
        return Field.builder()
                .name(name)
                .build();
    }

    public static Field of(String name, User user) {
        return Field.builder()
                .name(name)
                .user(user)
                .build();
    }
}
