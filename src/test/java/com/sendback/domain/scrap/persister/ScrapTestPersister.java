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

    public ScrapBuilder builder() {
        return new ScrapBuilder();
    }

    public final class ScrapBuilder {

        private User user;
        private Project project;

        public ScrapBuilder user(User user) {
            this.user = user;
            return this;
        }

        public ScrapBuilder project(Project project) {
            this.project = project;
            return this;
        }

        public Scrap save() {
            Scrap scrap = Scrap.of(
                    (user == null ? userTestPersister.builder().save() : user),
                    (project == null ? projectTestPersister.builder().save() : project)
            );
            return scrapRepository.save(scrap);
        }

    }



}
