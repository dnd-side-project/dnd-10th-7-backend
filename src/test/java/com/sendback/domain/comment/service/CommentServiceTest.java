package com.sendback.domain.comment.service;

import com.sendback.domain.comment.dto.request.SaveCommentRequestDto;
import com.sendback.domain.comment.dto.response.DeleteCommentResponseDto;
import com.sendback.domain.comment.dto.response.GetCommentsResponseDto;
import com.sendback.domain.comment.dto.response.SaveCommentResponseDto;
import com.sendback.domain.comment.entity.Comment;
import com.sendback.domain.comment.repository.CommentRepository;
import com.sendback.domain.project.entity.Project;
import com.sendback.domain.project.repository.ProjectRepository;
import com.sendback.domain.user.entity.User;
import com.sendback.domain.user.repository.UserRepository;
import com.sendback.global.ServiceTest;
import com.sendback.global.exception.type.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static com.sendback.domain.comment.exception.CommentExceptionType.NOT_COMMENT_AUTHOR;
import static com.sendback.domain.comment.persister.CommentFixture.createDummyComment;
import static com.sendback.domain.project.fixture.ProjectFixture.createDummyProject;
import static com.sendback.domain.user.fixture.UserFixture.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

public class CommentServiceTest extends ServiceTest {

    @InjectMocks
    CommentService commentService;
    @Mock
    CommentRepository commentRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    ProjectRepository projectRepository;
    private User user;
    private Project project;
    private Comment comment;

    @BeforeEach
    public void setUp() {
        this.user = createDummyUser();
        this.project = createDummyProject(user);
        this.comment = createDummyComment(user, project);
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

    @Test
    @DisplayName("댓글 리스트를 조회한다.")
    public void getCommentList_success() throws Exception {
        // given
        Long mock_userId = 1L;
        Long mock_projectId = 1L;

        given(projectRepository.findById(mock_projectId)).willReturn(Optional.of(project));
        List<Comment> commentList = new ArrayList<>();
        commentList.add(comment);
        given(commentRepository.findByProjectIdAndIsDeletedFalseOrderByCreatedAtDesc(project.getId())).willReturn(commentList);

        // when
        List<GetCommentsResponseDto> responseDto = commentService.getCommentList(mock_userId, mock_projectId);

        // then
        assertThat(responseDto.get(0).nickname()).isEqualTo(user.getNickname());
        assertThat(responseDto.get(0).content()).isEqualTo(comment.getContent());
        assertThat(responseDto.get(0).profileImageUrl()).isEqualTo(user.getProfileImageUrl());
    }

    @Nested
    @DisplayName("댓글 삭제")
    class deleteComment{

        @Test
        @DisplayName("댓글 작성자가 맞을 경우")
        public void deleteComment_success() throws Exception {
            //given
            Comment mock_comment = Comment.of("테스트", createDummyUser_D(), project);
            given(userRepository.findById(any())).willReturn(Optional.of(createDummyUser_D()));
            given(commentRepository.findById(any())).willReturn(Optional.of(mock_comment));

            //when
            DeleteCommentResponseDto responseDtos = commentService.deleteComment(createDummyUser_D().getId(),
                    mock_comment.getId());

            // then
            assertThat(responseDtos.isDeleted()).isTrue();
        }

        @Test
        @DisplayName("댓글 작성자가 아닐 경우")
        public void deleteComment_fail() throws Exception {
            //given
            Comment mock_comment = Comment.of("테스트", createDummyUser_D(), project);
            given(userRepository.findById(any())).willReturn(Optional.of(createDummyUser_E()));
            given(commentRepository.findById(any())).willReturn(Optional.of(mock_comment));

            //when, then
            assertThatThrownBy(() -> commentService.deleteComment(createDummyUser_E().getId(),
                    mock_comment.getId()))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage(NOT_COMMENT_AUTHOR.getMessage());

        }
    }
}
