package com.sendback.domain.comment.entity;

import com.sendback.domain.project.entity.Project;
import com.sendback.domain.user.entity.*;
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
@SQLDelete(sql = "UPDATE comment SET is_deleted = true WHERE id = ?")
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    private boolean isDeleted = false;

    @Builder
    public Comment(Long id, String content, User user, Project project ) {
        this.content = content;
        this.user = user;
        this.project = project;
    }

    public static Comment of(String content, User user, Project project){
        return Comment.builder()
                .content(content)
                .user(user)
                .project(project)
                .build();
    }
}
