package com.sendback.domain.like.service;

import com.sendback.domain.like.dto.response.ReactLikeResponse;
import com.sendback.domain.like.entity.Like;
import com.sendback.domain.like.repository.LikeRepository;
import com.sendback.domain.project.entity.Project;
import com.sendback.domain.project.service.ProjectService;
import com.sendback.domain.user.entity.User;
import com.sendback.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeService {

    private final UserService userService;
    private final ProjectService projectService;
    private final LikeRepository likeRepository;

    @Transactional
    public ReactLikeResponse react(Long userId, Long projectId) {
        User loginUser = userService.getUserById(userId);
        Project project = projectService.getProjectById(projectId);

        Like like = reactLike(loginUser, project);

        return new ReactLikeResponse(!like.isDeleted());
    }

    private Like reactLike(User user, Project project) {
        Optional<Like> likeOptional = likeRepository.findByUserAndProject(user, project);
        if (likeOptional.isEmpty()) {
            Like like = Like.of(user, project);
            return likeRepository.save(like);
        }
        return likeOptional.get().react();
    }
}

