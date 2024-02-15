package com.sendback.domain.like.repository;

import com.sendback.domain.like.entity.Like;
import com.sendback.domain.project.entity.Project;
import com.sendback.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByUserAndProject(User user, Project project);

    @Query("Select COUNT(l) From Like l where l.project IN :projects")
    Long countByProjects(@Param("projects") List<Project> projects);
}
