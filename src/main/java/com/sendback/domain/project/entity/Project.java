package com.sendback.domain.project.entity;

import com.sendback.domain.field.entity.Field;
import com.sendback.domain.project.dto.request.SaveProjectRequest;
import com.sendback.domain.project.dto.request.UpdateProjectRequest;
import com.sendback.domain.user.entity.User;
import com.sendback.global.common.BaseEntity;
import com.sendback.global.common.constants.Progress;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE project SET is_deleted = true WHERE id = ?")
public class Project extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_id")
    private Field field;

    private String title;

    private String content;

    private String summary;

    private String demoSiteUrl;

    private LocalDate startedAt;

    private LocalDate endedAt;

    @Enumerated(EnumType.STRING)
    private Progress progress;

    @Embedded
    private ProjectParticipantCount projectParticipantCount;

    @Embedded
    private ProjectPull projectPull;

    private boolean isFinished;

    private boolean isDeleted = false;

    @Builder
    private Project(
            final User user,
            final Field field,
            final String title,
            final String content,
            final String summary,
            final String demoSiteUrl,
            final LocalDate startedAt,
            final LocalDate endedAt,
            final Progress progress,
            final long plannerCount,
            final long frontendCount,
            final long backendCount,
            final long designCount,
            final boolean pullUpCnt,
            final long isPulledUp,
            final LocalDateTime pullEndAt,
            final boolean isFinished
    ) {
        this.user = user;
        this.field = field;
        this.title = title;
        this.content = content;
        this.summary = summary;
        this.demoSiteUrl = demoSiteUrl;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.progress = progress;
        this.projectParticipantCount = new ProjectParticipantCount(plannerCount, frontendCount, backendCount, designCount);
        this.projectPull = new ProjectPull(pullUpCnt, isPulledUp, pullEndAt);
        this.isFinished = isFinished;
    }

    public static Project of(User user, Field field, SaveProjectRequest saveProjectRequest) {
        return Project.builder()
                .user(user)
                .field(field)
                .title(saveProjectRequest.title())
                .content(saveProjectRequest.content())
                .summary(saveProjectRequest.summary())
                .demoSiteUrl(saveProjectRequest.demoSiteUrl())
                .startedAt(saveProjectRequest.startedAt())
                .endedAt(saveProjectRequest.endedAt())
                .progress(Progress.toEnum(saveProjectRequest.progress()))
                .plannerCount(saveProjectRequest.plannerCount())
                .frontendCount(saveProjectRequest.frontendCount())
                .backendCount(saveProjectRequest.backendCount())
                .designCount(saveProjectRequest.designCount())
                .isFinished(false)
                .build();
    }

    public void updateProject(Field field, UpdateProjectRequest updateProjectRequest) {
        this.field = field;
        this.title = updateProjectRequest.title();
        this.content = updateProjectRequest.content();
        this.summary = updateProjectRequest.summary();
        this.demoSiteUrl = updateProjectRequest.demoSiteUrl();
        this.startedAt = updateProjectRequest.startedAt();
        this.endedAt = updateProjectRequest.endedAt();
        this.progress = Progress.toEnum(updateProjectRequest.progress());
        this.projectParticipantCount = ProjectParticipantCount.of(updateProjectRequest);
    }

    public boolean isAuthor(final User user) {
        return Objects.equals(this.user, user);
    }

}
