package com.sparta.actionboss.domain.post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.actionboss.domain.post.entity.Post;
import com.sparta.actionboss.global.security.UserDetailsImpl;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PostResponseDto {
    private final Long postId;
    private final String title;
    private final String content;
    private final List<String> imageUrlList;
    private final String address;
    private final Double latitude;
    private final Double longitude;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final LocalDateTime createdAt;
    private final String nickname;
    private final int doneCount;
    private final boolean done;
    private final boolean owner;

    public PostResponseDto(Post post, List<String> imageURLs, boolean done, boolean owner) {
        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.imageUrlList = imageURLs;
        this.address = post.getAddress();
        this.latitude = post.getLatitude();
        this.longitude = post.getLongitude();
        this.createdAt = post.getCreatedAt();
        this.nickname = post.getUser().getNickname();
        this.doneCount = post.getPostDoneList().size();
        this.done = done;
        this.owner = owner;
    }
}
