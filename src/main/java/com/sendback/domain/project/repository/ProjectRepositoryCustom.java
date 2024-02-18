package com.sendback.domain.project.repository;

import com.sendback.domain.project.entity.Project;
import com.sendback.domain.user.dto.response.RegisteredProjectResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProjectRepositoryCustom {

    Page<RegisteredProjectResponseDto> findAllProjectsByMe(Pageable pageable, Long userId, Boolean isFinished);
    Page<Project> findAllByPageableAndFieldAndIsFinishedAndSort(
            Pageable pageable, String keyword, String field, Boolean isFinished, Long sort);
}
