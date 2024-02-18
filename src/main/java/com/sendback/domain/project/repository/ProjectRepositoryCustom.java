package com.sendback.domain.project.repository;

import com.sendback.domain.project.dto.response.RecommendedProjectResponseDto;
import com.sendback.domain.project.entity.Project;
import com.sendback.domain.user.dto.response.RegisteredProjectResponseDto;
import com.sendback.global.common.constants.FieldName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProjectRepositoryCustom {

    Page<RegisteredProjectResponseDto> findAllProjectsByMe(Pageable pageable, Long userId, Boolean isFinished);
    List<RecommendedProjectResponseDto> findRecommendedProjects(Long userId, List<FieldName> fieldNameList);
    Page<Project> findAllByPageableAndFieldAndIsFinishedAndSort(
            Pageable pageable, String keyword, String field, Boolean isFinished, Long sort);
}
