package com.sparta.actionboss.domain.post.entity;

import com.sparta.actionboss.domain.auth.entity.User;
import com.sparta.actionboss.domain.post.entity.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class PostDone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "done_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public PostDone(User user, Post post) {
        this.post = post;
        this.user = user;
    }
}
