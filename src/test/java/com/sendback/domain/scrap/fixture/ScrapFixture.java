package com.sendback.domain.scrap.fixture;

import com.sendback.domain.project.entity.Project;
import com.sendback.domain.scrap.entity.Scrap;
import com.sendback.domain.user.entity.User;

public class ScrapFixture {

    public static Scrap createDummyScrap(User user, Project project) {
        return Scrap.of(user, project);
    }

}
