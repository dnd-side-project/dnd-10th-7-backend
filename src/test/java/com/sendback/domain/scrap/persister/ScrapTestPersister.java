package com.sendback.domain.scrap.persister;

import com.sendback.domain.project.entity.Project;
import com.sendback.domain.project.persister.ProjectTestPersister;
import com.sendback.domain.scrap.entity.Scrap;
import com.sendback.domain.scrap.repository.ScrapRepository;
import com.sendback.domain.user.entity.User;
import com.sendback.domain.user.persister.UserTestPersister;
import com.sendback.global.Persister;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Persister
public class ScrapTestPersister {

    private final ScrapRepository scrapRepository;
    private final UserTestPersister userTestPersister;
    private final ProjectTestPersister projectTestPersister;


    private User user;
    private Project project;

    public ScrapTestPersister user(User user) {
        this.user = user;
        return this;
    }

    public ScrapTestPersister project(Project project) {
        this.project = project;
        return this;
    }

    public Scrap save() {
        Scrap scrap = Scrap.of(
                (user == null ? userTestPersister.save() : user),
                (project == null ? projectTestPersister.save() : project)
        );
        return scrapRepository.save(scrap);
    }
}
