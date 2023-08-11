package com.sparta.actionboss.domain.post.dto;

public record PostListResponseDto (

    Long postId,
    String title,
    String nickname,
    Double latitude,
    Double longitude,
    String address,
    String thumbnail
    // TODO : likeCount


) {

}