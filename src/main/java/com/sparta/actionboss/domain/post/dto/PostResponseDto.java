package com.sparta.actionboss.domain.post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.actionboss.domain.post.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PostResponseDto {
    private Long postId;
    private String title;
    private String content;
    private List<String> imageUrlList;
    private String address;
    private Double latitude;
    private Double longitude;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;
    private String nickname;
    private int doneCount;

    public PostResponseDto(Post post, List<String> imageURLs) {
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
    }
}
