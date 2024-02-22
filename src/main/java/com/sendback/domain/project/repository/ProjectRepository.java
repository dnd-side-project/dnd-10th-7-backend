package com.sendback.domain.project.repository;

import com.sendback.domain.project.entity.Project;
import com.sendback.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long>, ProjectRepositoryCustom {
    Long countByUserAndIsDeletedIsFalse(User user);
    List<Project> findByUserAndIsDeletedIsFalse(User user);
    List<Project> findAllByEndedAtBeforeAndIsDeletedIsFalse(LocalDate localDate);

}
