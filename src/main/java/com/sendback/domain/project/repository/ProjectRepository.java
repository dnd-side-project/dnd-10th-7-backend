package com.sendback.domain.project.repository;

import com.sendback.domain.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("Select COUNT(p) from Project p where p.user.id = :userId")
    Long countByUserId(@Param("userId") Long userId);
    List<Project> findByUserId(Long userId);
}
