package com.sendback.domain.like.persister;

import com.sendback.domain.like.entity.Like;
import com.sendback.domain.like.repository.LikeRepository;
import com.sendback.domain.project.entity.Project;
import com.sendback.domain.project.persister.ProjectTestPersister;
import com.sendback.domain.user.entity.User;
import com.sendback.domain.user.persister.UserTestPersister;
import com.sendback.global.Persister;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Persister
public class LikeTestPersister {

    private final LikeRepository likeRepository;
    private final UserTestPersister userTestPersister;
    private final ProjectTestPersister projectTestPersister;


    private User user;
    private Project project;

    public LikeTestPersister user(User user) {
        this.user = user;
        return this;
    }

    public LikeTestPersister project(Project project) {
        this.project = project;
        return this;
    }

    public Like save() {
        Like like = Like.of(
                (user == null ? userTestPersister.save() : user),
                (project == null ? projectTestPersister.save() : project)
        );
        return likeRepository.save(like);
    }
}
