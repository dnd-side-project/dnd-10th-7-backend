package com.sendback.domain.project.entity;

import com.sendback.domain.comment.entity.Comment;
import com.sendback.domain.feedback.entity.Feedback;
import com.sendback.domain.like.entity.Like;
import com.sendback.domain.project.dto.request.SaveProjectRequestDto;
import com.sendback.domain.project.dto.request.UpdateProjectRequestDto;
import com.sendback.domain.scrap.entity.Scrap;
import com.sendback.domain.user.entity.User;
import com.sendback.global.common.BaseEntity;
import com.sendback.global.common.constants.FieldName;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.SQLDelete;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

    @Enumerated(EnumType.STRING)
    private FieldName fieldName;

    private String title;

    @Column(length = 500)
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

    @Formula("(select count(*) from likes where likes.project_id=id and likes.is_deleted = false)")
    private int likeCount;

    @Formula("(select count(*) from feedback where feedback.project_id=id and feedback.is_finished = false and feedback.is_deleted = false)")
    private int feedbackCount;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Scrap> scraps = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectImage> projectImages = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Feedback> feedbacks = new ArrayList<>();

    @Builder
    private Project(
            final User user,
            final FieldName fieldName,
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
            final long pullUpCnt,
            final boolean isPulledUp,
            final LocalDateTime pulledAt,
            final boolean isFinished
    ) {
        this.user = user;
        this.fieldName = fieldName;
        this.title = title;
        this.content = content;
        this.summary = summary;
        this.demoSiteUrl = demoSiteUrl;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.progress = progress;
        this.projectParticipantCount = new ProjectParticipantCount(plannerCount, frontendCount, backendCount, designCount);
        this.projectPull = new ProjectPull(pullUpCnt, isPulledUp, pulledAt);
        this.isFinished = isFinished;
    }

    public static Project of(User user, SaveProjectRequestDto saveProjectRequestDto) {
        return Project.builder()
                .user(user)
                .fieldName(FieldName.toEnum(saveProjectRequestDto.field()))
                .title(saveProjectRequestDto.title())
                .content(saveProjectRequestDto.content())
                .summary(saveProjectRequestDto.summary())
                .demoSiteUrl(saveProjectRequestDto.demoSiteUrl())
                .startedAt(saveProjectRequestDto.startedAt())
                .endedAt(saveProjectRequestDto.endedAt())
                .progress(Progress.toEnum(saveProjectRequestDto.progress()))
                .plannerCount(saveProjectRequestDto.plannerCount())
                .frontendCount(saveProjectRequestDto.frontendCount())
                .backendCount(saveProjectRequestDto.backendCount())
                .designCount(saveProjectRequestDto.designCount())
                .pullUpCnt(0L)
                .isPulledUp(false)
                .pulledAt(LocalDateTime.now())
                .isFinished(false)
                .build();
    }

    public void updateProject(FieldName fieldName, UpdateProjectRequestDto updateProjectRequestDto) {
        this.fieldName = fieldName;
        this.title = updateProjectRequestDto.title();
        this.content = updateProjectRequestDto.content();
        this.summary = updateProjectRequestDto.summary();
        this.demoSiteUrl = updateProjectRequestDto.demoSiteUrl();
        this.startedAt = updateProjectRequestDto.startedAt();
        this.endedAt = updateProjectRequestDto.endedAt();
        this.progress = Progress.toEnum(updateProjectRequestDto.progress());
        this.projectParticipantCount = ProjectParticipantCount.of(updateProjectRequestDto);
    }

    public boolean isAuthor(final User user) {
        return Objects.equals(this.user, user);
    }

    public boolean isAvailablePulledUp() {
        return (this.projectPull.getPulledAt().isBefore(LocalDateTime.now().minusDays(3))
                || !this.getProjectPull().isPulledUp());
    }

    public boolean isOverPullUpCnt() {
        return (this.projectPull.getPullUpCnt() > 15);
    }

    public void pullUp() {
        this.projectPull.pullUp();
    }

    public void updateIsFinished() {
        this.isFinished = true;
    }

}
