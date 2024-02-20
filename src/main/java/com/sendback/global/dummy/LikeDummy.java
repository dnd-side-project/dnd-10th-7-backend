package com.sendback.global.dummy;

import com.sendback.domain.like.entity.Like;
import com.sendback.domain.like.repository.LikeRepository;
import com.sendback.domain.project.entity.Project;
import com.sendback.domain.project.repository.ProjectRepository;
import com.sendback.domain.user.entity.User;
import com.sendback.domain.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component("likeDummy")
@DependsOn("projectDummy")
@RequiredArgsConstructor
@Transactional
@Slf4j
public class LikeDummy {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    @PostConstruct
    public void init() {
        if (likeRepository.count() > 0) {
            log.info("[likeDummy] 좋아요 데이터가 이미 존재");
        } else {
            createLikes();
            log.info("[likeDummy] 좋아요 더미 생성 완료");
        }
    }

    private void createLikes() {
        List<User> users = userRepository.findAll();

        List<Project> projects = projectRepository.findAll();

        List<Boolean> choice = List.of(Boolean.TRUE, Boolean.FALSE);

        users.forEach(
                user -> projects.stream().filter(project -> choice.get((int) (Math.random() * 100) % choice.size()).equals(Boolean.TRUE))
                        .map(project -> Like.of(user, project)).forEach(likeRepository::save));
    }
}
