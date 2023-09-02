package com.sparta.actionboss.domain.auth.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String refreshToken;

    @Column(nullable = false)
    private String nickname;

    public RefreshToken(String refreshToken, String nickname){
        this.refreshToken = refreshToken;
        this.nickname = nickname;
    }

    public void updateToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
