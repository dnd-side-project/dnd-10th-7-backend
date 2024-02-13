package com.sendback.domain.like.fixture;

import com.sendback.domain.like.entity.Like;
import com.sendback.domain.project.entity.Project;
import com.sendback.domain.user.entity.User;

public class LikeFixture {

    public static Like createDummyLike(User user, Project project) {
        return Like.of(user, project);
    }
}
