package com.sendback.domain.comment.persister;

import com.sendback.domain.comment.entity.Comment;
import com.sendback.domain.project.entity.Project;
import com.sendback.domain.user.entity.User;

import static com.sendback.domain.project.fixture.ProjectFixture.createDummyProject;
import static com.sendback.domain.user.fixture.UserFixture.createDummyUser;

public class CommentFixture {

    public static Comment createDummyComment(User user, Project project){
        return Comment.of("안녕", user, project);
    }
}
