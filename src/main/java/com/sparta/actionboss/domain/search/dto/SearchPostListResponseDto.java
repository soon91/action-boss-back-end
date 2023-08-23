package com.sparta.actionboss.domain.search.dto;

public record SearchPostListResponseDto (
        Long postId,
        String title,
        Integer agreeCount,
        String nickname,
        String address,
        String thumbnail
) {

}