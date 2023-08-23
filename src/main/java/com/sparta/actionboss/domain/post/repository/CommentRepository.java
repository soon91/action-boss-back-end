package com.sparta.actionboss.domain.post.repository;

import com.sparta.actionboss.domain.post.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT i FROM Comment i WHERE i.post.postId = :postId")
    List<Comment> findCommentsByPostId(Long postId);
}
