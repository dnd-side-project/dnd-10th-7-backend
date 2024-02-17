package com.sendback.domain.project.repository;

import com.sendback.domain.user.dto.response.RegisteredProjectResponseDto;
import com.sendback.domain.user.dto.response.ScrappedProjectResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProjectRepositoryCustom {

    Page<RegisteredProjectResponseDto> findAllRegisteredProjectsByMe(Pageable pageable, Long userId, Boolean isFinished);

    Page<ScrappedProjectResponseDto> findAllScrappedProjectsByMe(Pageable pageable, Long userId, Boolean isFinished);

}
