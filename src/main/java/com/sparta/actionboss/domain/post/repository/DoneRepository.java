package com.sparta.actionboss.domain.post.repository;

import com.sparta.actionboss.domain.auth.entity.User;
import com.sparta.actionboss.domain.post.entity.Done;
import com.sparta.actionboss.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoneRepository extends JpaRepository<Done, Long> {
    Optional<Done> findByPostAndUser(Post post, User user);
}
