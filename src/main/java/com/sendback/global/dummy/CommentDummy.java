package com.sendback.global.dummy;

import com.sendback.domain.comment.entity.Comment;
import com.sendback.domain.comment.repository.CommentRepository;
import com.sendback.domain.project.entity.Project;
import com.sendback.domain.project.repository.ProjectRepository;
import com.sendback.domain.scrap.entity.Scrap;
import com.sendback.domain.scrap.repository.ScrapRepository;
import com.sendback.domain.user.entity.User;
import com.sendback.domain.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Component("commentDummy")
@DependsOn("projectDummy")
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CommentDummy {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    @PostConstruct
    public void init() {
        if (commentRepository.count() > 0) {
            log.info("[commentDummy] 댓글 데이터가 이미 존재");
        } else {
            createComments();
            log.info("[commentDummy] 댓글 더미 생성 완료");
        }
    }

    private void createComments() {
        List<User> users = userRepository.findAll();
        List<Project> projects = projectRepository.findAll();
        Random random = new Random();

        for (int i = 1; i <= 20; i++) {
            User randomUser = users.get(random.nextInt(users.size()));
            boolean randomIsDeleted = random.nextBoolean();
            Project randomProject = projects.get(random.nextInt(projects.size()));
            Comment comment = Comment.of("안녕하세요", randomUser, randomProject, randomIsDeleted);
            commentRepository.save(comment);
        }
    }

}
