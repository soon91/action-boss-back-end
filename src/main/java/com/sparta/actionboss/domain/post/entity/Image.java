package com.sparta.actionboss.domain.post.entity;

import com.sparta.actionboss.global.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Image extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(nullable = false)
    private String imageName;

    @Column(nullable = false)
    private String folderName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    public Image(String imageName, String folderName, Post post) {
        this.imageName = imageName;
        this.folderName = folderName;
        this.post = post;
    }
}
