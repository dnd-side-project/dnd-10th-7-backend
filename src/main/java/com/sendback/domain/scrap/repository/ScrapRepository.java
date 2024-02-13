package com.sendback.domain.scrap.repository;

import com.sendback.domain.project.entity.Project;
import com.sendback.domain.scrap.entity.Scrap;
import com.sendback.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {

    Optional<Scrap> findByUserAndProject(User user, Project project);
}
