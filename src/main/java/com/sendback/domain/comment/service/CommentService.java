package com.sendback.domain.comment.service;

import com.sendback.domain.comment.dto.request.SaveCommentRequestDto;
import com.sendback.domain.comment.dto.response.SaveCommentResponseDto;
import com.sendback.domain.comment.entity.Comment;
import com.sendback.domain.comment.repository.CommentRepository;
import com.sendback.domain.project.entity.Project;
import com.sendback.domain.project.repository.ProjectRepository;
import com.sendback.domain.user.entity.User;
import com.sendback.domain.user.repository.UserRepository;
import com.sendback.global.exception.type.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static com.sendback.domain.project.exception.ProjectExceptionType.NOT_FOUND_PROJECT;
import static com.sendback.domain.user.exception.UserExceptionType.NOT_FOUND_USER;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    public SaveCommentResponseDto saveComment(Long userId, Long projectId, SaveCommentRequestDto saveCommentRequestDto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(NOT_FOUND_USER)
        );

        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new NotFoundException(NOT_FOUND_PROJECT)
        );

        Comment comment = Comment.of(saveCommentRequestDto.content(), user, project);
        Comment savedComment = commentRepository.save(comment);
        SaveCommentResponseDto responseDto = new SaveCommentResponseDto(savedComment.getId(), savedComment.getContent(),
                user.getId(), user.getProfileImageUrl(), user.getNickname(), savedComment.getCreatedAt());
        return responseDto;
    }
}
