package com.sendback.domain.project.entity;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProjectPull {

    private Long pullUpCnt;
    private boolean isPulledUp;
    private LocalDateTime pulledAt;

    public void pullUp() {
        this.pulledAt = LocalDateTime.now();
        this.isPulledUp = true;
        this.pullUpCnt += 1;
    }
}
