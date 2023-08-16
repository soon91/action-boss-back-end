package com.sparta.actionboss.domain.post.entity;

import com.sparta.actionboss.domain.auth.entity.User;
import com.sparta.actionboss.domain.done.entity.PostDone;
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
    private List<String> imageNames;

    @Column(nullable = false)
    private boolean done;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<PostDone> postDoneList;

    public Post(PostRequestDto postRequestDto, List<String> imageNames, User user) {
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
        this.imageNames = imageNames;
        this.address = postRequestDto.getAddress();
        this.latitude = postRequestDto.getLatitude();
        this.longitude = postRequestDto.getLongitude();
        this.user = user;
    }

    public void update(PostRequestDto postRequestDto) {
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
    }

    public void setNames(List<String> imageNames) {
        this.imageNames = imageNames;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
