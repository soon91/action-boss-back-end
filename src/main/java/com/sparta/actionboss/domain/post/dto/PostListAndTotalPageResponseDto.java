package com.sparta.actionboss.domain.post.dto;

import java.util.List;

public record PostListAndTotalPageResponseDto<T> (
        List<T> postList,
        Integer totalPage,
        Integer presentPage
) {

}


