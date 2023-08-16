package com.sparta.actionboss.domain.post.dto;

public record PostListResponseDto (
    Long postId,
    String title,
    Integer agreeCount,
    String nickname,
    String address,
    String thumbnail
) {

}