package com.sendback.domain.like.repository;

import com.sendback.domain.like.entity.Like;
import com.sendback.domain.project.entity.Project;
import com.sendback.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByUserAndProject(User user, Project project);
    boolean existsByUserAndProjectAndIsDeletedIsFalse(User user, Project project);
    Long countByProjectInAndIsDeletedIsFalse(List<Project> projects);
}
