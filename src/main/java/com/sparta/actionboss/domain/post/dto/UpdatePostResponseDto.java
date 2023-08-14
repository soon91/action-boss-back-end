package com.sparta.actionboss.domain.post.dto;

import lombok.Getter;

@Getter
public class UpdatePostResponseDto {
    private final int statusCode = 200;
    private final String msg = "민원글 수정에 성공하였습니다.";
}
