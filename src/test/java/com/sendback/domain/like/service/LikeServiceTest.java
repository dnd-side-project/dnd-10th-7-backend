package com.sendback.domain.like.service;

import com.sendback.domain.like.dto.response.ReactLikeResponse;
import com.sendback.domain.like.entity.Like;
import com.sendback.domain.like.repository.LikeRepository;
import com.sendback.domain.project.entity.Project;
import com.sendback.domain.project.service.ProjectService;
import com.sendback.domain.user.entity.User;
import com.sendback.domain.user.service.UserService;
import com.sendback.global.ServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static com.sendback.domain.like.fixture.LikeFixture.createDummyLike;
import static com.sendback.domain.project.fixture.ProjectFixture.createDummyProject;
import static com.sendback.domain.user.fixture.UserFixture.createDummyUser;
import static org.assertj.core.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.spy;

public class LikeServiceTest extends ServiceTest {

    @InjectMocks
    LikeService likeService;

    @Mock
    LikeRepository likeRepository;

    @Mock
    UserService userService;

    @Mock
    ProjectService projectService;

    private User user;
    private Project project;
    private Like like;

    @BeforeEach
    public void setUp() {
        this.user = spy(createDummyUser());
        this.project = spy(createDummyProject(user));
        this.like = spy(createDummyLike(user, project));
    }

    @Nested
    @DisplayName("좋아요 반응을 했을 때")
    class react {

        @Test
        @DisplayName("기존의 좋아요를 하지 않았으면 새롭게 저장한다.")
        public void success() throws Exception {
            //given
            given(userService.getUserById(anyLong())).willReturn(user);
            given(projectService.getProjectById(anyLong())).willReturn(project);
            given(likeRepository.findByUserAndProject(any(User.class), any(Project.class))).willReturn(Optional.empty());
            given(likeRepository.save(any(Like.class))).willReturn(like);

            //when
            ReactLikeResponse reactLikeResponse = likeService.react(1L, 1L);

            //then
            assertThat(reactLikeResponse.isReacted()).isTrue();
        }

        @Test
        @DisplayName("좋아요를 취소하는 요청이면 soft delete 한다.")
        public void softDelete() throws Exception {
            //given
            given(userService.getUserById(anyLong())).willReturn(user);
            given(projectService.getProjectById(anyLong())).willReturn(project);
            given(likeRepository.findByUserAndProject(any(User.class), any(Project.class))).willReturn(Optional.of(like));

            //when
            ReactLikeResponse reactLikeResponse = likeService.react(1L, 1L);

            //then
            assertThat(reactLikeResponse.isReacted()).isFalse();
        }

        @Test
        @DisplayName("취소했던 좋아요를 다시 반응하는 요청이면 삭제 처리를 취소한다.")
        public void visible() throws Exception {
            //given
            like.react();   // 취소된 like

            given(userService.getUserById(anyLong())).willReturn(user);
            given(projectService.getProjectById(anyLong())).willReturn(project);
            given(likeRepository.findByUserAndProject(any(User.class), any(Project.class))).willReturn(Optional.of(like));

            //when
            ReactLikeResponse reactLikeResponse = likeService.react(1L, 1L);

            //then
            assertThat(reactLikeResponse.isReacted()).isTrue();
        }
    }
}
