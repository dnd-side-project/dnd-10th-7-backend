package com.sendback.domain.feedback.entity;

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
@SQLDelete(sql = "UPDATE feedback_submit SET is_deleted = true WHERE id = ?")
public class FeedbackSubmit extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedback_id")
    private Feedback feedback;

    private String screenShotUrl;
    private boolean isDeleted = false;

    @Builder
    private FeedbackSubmit(
            final User user,
            final Feedback feedback,
            final String screenShotUrl
    ) {
        this.user = user;
        this.feedback = feedback;
        this.screenShotUrl = screenShotUrl;
    }

    public static FeedbackSubmit of(User user, Feedback feedback, String screenShotUrl) {
        return FeedbackSubmit.builder()
                .user(user)
                .feedback(feedback)
                .screenShotUrl(screenShotUrl)
                .build();
    }

}
