package com.sparta.actionboss.domain.post.dto;

public record PostListResponseDto (

    Long postId,
    String title,
    String nickname,
    String address,
    String thumbnail
    // TODO : likeCount


) {

}