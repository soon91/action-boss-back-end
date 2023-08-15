package com.sparta.actionboss.domain.done.dto;

import lombok.Getter;

@Getter
public class PostDoneResponseDto {
    private final int statusCode = 200;
    private final String msg = "해결했어요 버튼 누르기 성공";
}
