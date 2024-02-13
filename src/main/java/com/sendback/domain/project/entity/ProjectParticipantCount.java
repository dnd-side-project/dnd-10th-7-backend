package com.sendback.domain.project.entity;

import com.sendback.domain.project.dto.request.UpdateProjectRequest;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProjectParticipantCount {

    private long plannerCount;
    private long frontendCount;
    private long backendCount;
    private long designCount;

    public static ProjectParticipantCount of(UpdateProjectRequest updateProjectRequest) {
        return new ProjectParticipantCount(updateProjectRequest.plannerCount(), updateProjectRequest.frontendCount(),
                updateProjectRequest.backendCount(), updateProjectRequest.designCount());
    }
}
