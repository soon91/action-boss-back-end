package com.sparta.actionboss.domain.post.repository;

import com.sparta.actionboss.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p WHERE p.done = true")
    Page<Post> findByDoneTrue(Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.done = false")
    Page<Post> findByDoneFalse(Pageable pageable);
}
