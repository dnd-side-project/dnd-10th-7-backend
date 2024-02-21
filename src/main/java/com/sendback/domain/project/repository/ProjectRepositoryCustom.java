package com.sendback.domain.project.repository;

import com.sendback.domain.project.entity.Project;
import com.sendback.domain.user.dto.response.RegisteredProjectResponseDto;
import com.sendback.domain.user.dto.response.ScrappedProjectResponseDto;
import com.sendback.domain.user.dto.response.SubmittedFeedbackResponseDto;
import com.sendback.global.common.constants.FieldName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProjectRepositoryCustom {

    Page<RegisteredProjectResponseDto> findAllRegisteredProjectsByMe(Pageable pageable, Long userId, Boolean isFinished);

    Page<ScrappedProjectResponseDto> findAllScrappedProjectsByMe(Pageable pageable, Long userId, Boolean isFinished);

    List<Project> findRecommendedProjects(List<FieldName> fieldNameList, int size);

    Page<Project> findAllByPageableAndFieldAndIsFinishedAndSort(
            Pageable pageable, String keyword, String field, Boolean isFinished, Long sort);
    Page<SubmittedFeedbackResponseDto> findAllSubmittedProjectsByMe(Pageable pageable, Long userId, Boolean isFinished);
}
