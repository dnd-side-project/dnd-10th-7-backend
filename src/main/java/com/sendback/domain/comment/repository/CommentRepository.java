package com.sendback.domain.comment.repository;

import com.sendback.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByProjectIdAndIsDeletedFalseOrderByCreatedAtDesc(Long projectId);
}
