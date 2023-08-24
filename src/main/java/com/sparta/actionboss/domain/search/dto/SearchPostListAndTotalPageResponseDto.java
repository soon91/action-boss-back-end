package com.sparta.actionboss.domain.search.dto;

import java.util.List;

public record SearchPostListAndTotalPageResponseDto<T> (
        List<T> postList,
        Integer totalPage,
        Integer presentPage
) {

}