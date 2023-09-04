package com.sparta.actionboss.domain.mypage.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sparta.actionboss.domain.post.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MyPagePostsResponseDto {
    private final Long postId;
    private final String title;
    private final Boolean done;
    private final int agreeCount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
    private final LocalDateTime postCreatedAt;

    public MyPagePostsResponseDto(Post post){
        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.done = post.isDone();
        this.agreeCount = post.getAgreeCount();
        this.postCreatedAt = post.getCreatedAt();
    }
}
