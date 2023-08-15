package com.sparta.actionboss.domain.post.repository;

import com.sparta.actionboss.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByDoneIsTrue(Pageable pageable);
    Page<Post> findByDoneIsFalse(Pageable pageable);

    List<Post> findByDoneIsTrue();
    List<Post> findByDoneIsFalse();
}
