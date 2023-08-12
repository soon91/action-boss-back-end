package com.sparta.actionboss.domain.post.repository;

import com.sparta.actionboss.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
}
