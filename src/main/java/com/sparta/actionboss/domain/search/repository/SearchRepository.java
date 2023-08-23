package com.sparta.actionboss.domain.search.repository;

import com.sparta.actionboss.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchRepository extends JpaRepository<Post, Long> {
    Page<Post> findByDoneIsTrueAndAddressContaining(String search, Pageable pageable);
    Page<Post> findByDoneIsFalseAndAddressContaining(String search, Pageable pageable);
}
