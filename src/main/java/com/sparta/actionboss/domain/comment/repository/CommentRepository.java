package com.sparta.actionboss.domain.comment.repository;

import com.sparta.actionboss.domain.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT i FROM Comment i WHERE i.post.postId = :postId")
    List<Comment> findCommentsByPostId(Long postId);

    @Query("SELECT c FROM Comment c WHERE c.user.userId = :userId ORDER BY c.createdAt DESC")
    Page<Comment> findCommentsByUserId(@Param("userId") Long userId, Pageable pageable);
}
