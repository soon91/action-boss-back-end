package com.sparta.actionboss.domain.auth.entity;

import com.sparta.actionboss.domain.mypage.dto.UpdateEmailRequestDto;
import com.sparta.actionboss.domain.post.entity.Comment;
import com.sparta.actionboss.domain.post.entity.Post;
import com.sparta.actionboss.global.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class User extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long userId;

    @Column(nullable = true, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;

    private Long kakaoId;

    @OneToMany(mappedBy = "user")
    private List<Post> postList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Comment> commentList = new ArrayList<>();

    public User(String nickname, String password, String email, UserRoleEnum role) {
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public User(String nickname, String password, String email, UserRoleEnum role, Long kakaoId) {
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.role = role;
        this.kakaoId = kakaoId;
    }

    public User(String nickname, String password, UserRoleEnum role, Long kakaoId) {
        this.nickname = nickname;
        this.password = password;
        this.role = role;
        this.kakaoId = kakaoId;
    }

    public User kakaoIdUpdate(Long kakaoId){
        this.kakaoId = kakaoId;
        return this;
    }

    public void updateEmail(UpdateEmailRequestDto requestDto){
        this.email = requestDto.getEmail();
    }
}