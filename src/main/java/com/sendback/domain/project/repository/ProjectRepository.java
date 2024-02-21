package com.sendback.domain.project.repository;

import com.sendback.domain.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long>, ProjectRepositoryCustom {
    Long countByUserId(Long userId);
    List<Project> findByUserId(Long userId);
    List<Project> findTop12ByOrderByLikeCountDesc();
    List<Project> findAllByEndedAtBeforeAndIsDeletedIsFalse(LocalDate localDate);

}
