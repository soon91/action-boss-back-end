package com.sparta.actionboss.domain.post.repository;

import com.sparta.actionboss.domain.auth.entity.User;
import com.sparta.actionboss.domain.post.entity.PostDone;
import com.sparta.actionboss.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostDoneRepository extends JpaRepository<PostDone, Long> {
    Optional<PostDone> findByPostAndUser(Post post, User user);
}
