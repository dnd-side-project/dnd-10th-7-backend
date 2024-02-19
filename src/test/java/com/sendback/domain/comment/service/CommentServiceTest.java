package com.sendback.domain.comment.service;

import com.sendback.domain.comment.dto.request.SaveCommentRequestDto;
import com.sendback.domain.comment.dto.response.SaveCommentResponseDto;
import com.sendback.domain.comment.entity.Comment;
import com.sendback.domain.comment.repository.CommentRepository;
import com.sendback.domain.project.entity.Project;
import com.sendback.domain.project.repository.ProjectRepository;
import com.sendback.domain.user.entity.User;
import com.sendback.domain.user.repository.UserRepository;
import com.sendback.global.ServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import java.util.Optional;
import static com.sendback.domain.project.fixture.ProjectFixture.createDummyProject;
import static com.sendback.domain.user.fixture.UserFixture.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.spy;
import static org.assertj.core.api.Assertions.*;
public class CommentServiceTest extends ServiceTest {

    @InjectMocks
    CommentService commentService;
    @Mock CommentRepository commentRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    ProjectRepository projectRepository;
    private User user;
    private Project project;

    @BeforeEach
    public void setUp() {
        this.user = spy(createDummyUser());
        this.project = spy(createDummyProject(user));
    }

    @Test
    @DisplayName("댓글을 등록한다.")
    public void saveComment_success() throws Exception {
        // given

        given(userRepository.findById(any())).willReturn(Optional.of(user));
        given(projectRepository.findById(any())).willReturn(Optional.of(project));
        Comment comment = Comment.of("테스트", user, project);
        given(commentRepository.save(any(Comment.class))).willReturn(comment);
        SaveCommentRequestDto saveCommentRequestDto = new SaveCommentRequestDto("테스트");

        // when
        SaveCommentResponseDto responseDto = commentService.saveComment(user.getId(), project.getId(), saveCommentRequestDto);

        // then
        assertThat(responseDto.nickname()).isEqualTo(user.getNickname());
        assertThat(responseDto.profileImageUrl()).isEqualTo(user.getProfileImageUrl());
        assertThat(responseDto.content()).isEqualTo(comment.getContent());
    }
}
