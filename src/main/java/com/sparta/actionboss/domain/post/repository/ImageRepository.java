package com.sparta.actionboss.domain.post.repository;

import com.sparta.actionboss.domain.post.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    @Query("SELECT i FROM Image i WHERE i.post.postId = :postId")
    List<Image> findImagesByPostId(@Param("postId") Long postId);
}
