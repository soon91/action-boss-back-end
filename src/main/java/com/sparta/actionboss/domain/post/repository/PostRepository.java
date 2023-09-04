package com.sparta.actionboss.domain.post.repository;

import com.sparta.actionboss.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT i FROM Post i WHERE i.user.userId = :userId")
    Page<Post> findPostByUserId(@Param("userId") Long userId, Pageable pageable);
}
