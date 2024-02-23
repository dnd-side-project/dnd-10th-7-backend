package com.sendback.domain.feedback.entity;


import com.sendback.domain.feedback.dto.request.SaveFeedbackRequestDto;
import com.sendback.domain.project.entity.Project;
import com.sendback.domain.user.entity.User;
import com.sendback.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE feedback SET is_deleted = true WHERE id = ?")
public class Feedback extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;
    private String title;
    private String linkUrl;
    @Column(length = 500)
    private String content;
    private String rewardMessage;
    private LocalDate startedAt;
    private LocalDate endedAt;
    private boolean isFinished = false;
    private boolean isDeleted = false;

    @Builder
    private Feedback(
            final User user,
            final Project project,
            final String title,
            final String linkUrl,
            final String content,
            final String rewardMessage,
            final LocalDate startedAt,
            final LocalDate endedAt
    ) {
        this.user = user;
        this.project = project;
        this.title = title;
        this.linkUrl = linkUrl;
        this.content = content;
        this.rewardMessage = rewardMessage;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
    }

    public static Feedback of(User user, Project project, SaveFeedbackRequestDto saveFeedbackRequestDto) {
        return Feedback.builder()
                .user(user)
                .project(project)
                .title(saveFeedbackRequestDto.title())
                .linkUrl(saveFeedbackRequestDto.linkUrl())
                .content(saveFeedbackRequestDto.content())
                .rewardMessage(saveFeedbackRequestDto.rewardMessage())
                .startedAt(saveFeedbackRequestDto.startedAt())
                .endedAt(saveFeedbackRequestDto.endedAt())
                .build();
    }

    public void updateIsFinished() {
        this.isFinished = true;
    }

    public boolean isAuthor(final User user) {
        return Objects.equals(this.user, user);
    }

}
