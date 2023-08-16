package com.sparta.actionboss.domain.post.repository;

import com.sparta.actionboss.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    // TODO : 비교 후 삭제 예정
//    Page<Post> findByDoneIsTrue(Pageable pageable);
//    Page<Post> findByDoneIsFalse(Pageable pageable);
//
//    Page<Post> findByDoneIsTrueAndLatitudeBetweenAndLongitudeBetween(
//            Double northLatitude, Double southLatitude,
//            Double westLongitude, Double eastLongitude,
//            Pageable pageable);
//
//    Page<Post> findByDoneIsFalseAndLatitudeBetweenAndLongitudeBetween(
//            Double northLatitude, Double southLatitude,
//            Double westLongitude, Double eastLongitude,
//            Pageable pageable);

    @Query("SELECT p FROM Post p " + "WHERE p.done = :done " +
            "AND p.latitude >= :south AND p.latitude <= :north " +
            "AND p.longitude >= :west AND p.longitude <= :east")
    Page<Post> findFilteredPosts(
            boolean done, Double north, Double east, Double south, Double west, Pageable pageable
    );

    List<Post> findByDoneIsTrue();
    List<Post> findByDoneIsFalse();
}
