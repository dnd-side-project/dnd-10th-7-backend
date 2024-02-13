package com.sendback.domain.project.repository;

import com.sendback.domain.project.entity.Project;
import com.sendback.domain.project.entity.ProjectImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectImageRepository extends JpaRepository<ProjectImage, Long> {
    Optional<ProjectImage> findByProjectAndImageUrl(Project project, String imageUrl);
    List<ProjectImage> findAllByProject(Project project);
}
