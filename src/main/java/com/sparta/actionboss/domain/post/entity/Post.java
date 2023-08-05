package com.sparta.actionboss.domain.post.entity;

import com.sparta.actionboss.domain.auth.entity.User;
import com.sparta.actionboss.global.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity
@Getter
public class Post extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long postId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @ElementCollection
    private List<String> image;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean done;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
