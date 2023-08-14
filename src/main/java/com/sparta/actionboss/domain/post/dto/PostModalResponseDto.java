package com.sparta.actionboss.domain.post.dto;

import com.sparta.actionboss.domain.post.entity.Post;
import lombok.Getter;

@Getter
public class PostModalResponseDto {
    private Long postId;
    private String title;
    private String thumbnail;
    private String address;
    private String nickname;
    // TODO : likeCount

    public PostModalResponseDto(Post post) {
        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.thumbnail = post.getImageUrls().get(0);
        this.address = post.getAddress();
        this.nickname = post.getUser().getNickname();
    }
}
