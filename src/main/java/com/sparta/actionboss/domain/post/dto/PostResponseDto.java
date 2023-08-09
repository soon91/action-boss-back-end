package com.sparta.actionboss.domain.post.dto;

import com.sparta.actionboss.domain.post.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponseDto {

    private Long postId;
    private String title;
    private String nickname;
    private String latitude;
    private String longitude;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    // TODO : 이미지 , likeCount

    // TODO : Post entity에 위,경도 추가
    public PostResponseDto(Post post) {
        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.nickname = post.getUser().getNickname();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
    }
}
