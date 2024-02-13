package com.sendback.domain.scrap.entity;

import com.sendback.domain.project.entity.Project;
import com.sendback.domain.user.entity.User;
import com.sendback.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE scrap SET is_deleted = true WHERE id = ?")
public class Scrap extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    private boolean isDeleted = false;

    @Builder
    private Scrap(
            final User user,
            final Project project) {
        this.user = user;
        this.project = project;
    }

    public static Scrap of(User user, Project project) {
        return Scrap.builder()
                .user(user)
                .project(project)
                .build();
    }

    public Scrap click() {
        this.isDeleted = !this.isDeleted;
        return this;
    }
}
