package com.sendback.domain.scrap.service;

import com.sendback.domain.project.entity.Project;
import com.sendback.domain.project.service.ProjectService;
import com.sendback.domain.scrap.dto.response.ClickScrapResponse;
import com.sendback.domain.scrap.entity.Scrap;
import com.sendback.domain.scrap.repository.ScrapRepository;
import com.sendback.domain.user.entity.User;
import com.sendback.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScrapService {

    private final UserService userService;
    private final ProjectService projectService;
    private final ScrapRepository scrapRepository;

    @Transactional
    public ClickScrapResponse clickScrap(Long userId, Long projectId) {
        User loginUser = userService.getUserById(userId);
        Project project = projectService.getProjectById(projectId);

        Scrap scrap = clickScrap(loginUser, project);

        return new ClickScrapResponse(!scrap.isDeleted());
    }

    private Scrap clickScrap(User user, Project project) {
        Optional<Scrap> scrapOptional = scrapRepository.findByUserAndProject(user, project);
        if (scrapOptional.isEmpty()) {
            Scrap scrap = Scrap.of(user, project);
            return scrapRepository.save(scrap);
        }
        return scrapOptional.get().click();
    }
}
