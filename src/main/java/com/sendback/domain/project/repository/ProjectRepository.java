package com.sendback.domain.project.repository;

import com.sendback.domain.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Long countByUserId(Long userId);
    List<Project> findByUserId(Long userId);
}
