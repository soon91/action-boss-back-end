package com.sparta.actionboss.domain.post.repository;

import com.sparta.actionboss.domain.auth.entity.User;
import com.sparta.actionboss.domain.post.entity.Agree;
import com.sparta.actionboss.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AgreeRepository extends JpaRepository<Agree, Long> {
    boolean existsAgreeByUserAndPost(User user, Post post);

    Optional<Agree> findByUserAndPost(User user, Post post);

    List<Agree> findByPost(Post post);
}
