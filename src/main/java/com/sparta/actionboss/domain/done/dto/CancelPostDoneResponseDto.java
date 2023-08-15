package com.sparta.actionboss.domain.done.dto;

import lombok.Getter;

@Getter
public class CancelPostDoneResponseDto {
    private final int statusCode = 200;
    private final String msg = "해결했어요 취소 성공";
}
