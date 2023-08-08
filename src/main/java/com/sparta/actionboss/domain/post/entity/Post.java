package com.sparta.actionboss.domain.post.entity;

import com.sparta.actionboss.domain.auth.entity.User;
import com.sparta.actionboss.domain.post.dto.PostRequestDto;
import com.sparta.actionboss.global.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Post extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long postId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    @ElementCollection
    private List<String> image;

    @Column(nullable = false)
    private boolean done;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Post(PostRequestDto postRequestDto) {
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
        this.latitude = postRequestDto.getLatitude();
        this.longitude = postRequestDto.getLongitude();
    }

    public void update(PostRequestDto postRequestDto) {
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
        this.latitude = postRequestDto.getLatitude();
        this.longitude = postRequestDto.getLongitude();
    }
}
