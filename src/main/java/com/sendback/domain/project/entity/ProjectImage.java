package com.sendback.domain.project.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;
    private String imageUrl;

    @Builder
    private ProjectImage(
            final Project project,
            final String imageUrl
    ) {
        this.project = project;
        this.imageUrl = imageUrl;
    }

    public static ProjectImage of(Project project, String imageUrl) {
        return ProjectImage.builder()
                .project(project)
                .imageUrl(imageUrl)
                .build();
    }
}

