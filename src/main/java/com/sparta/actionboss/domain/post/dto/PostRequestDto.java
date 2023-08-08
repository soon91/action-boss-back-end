package com.sparta.actionboss.domain.post.dto;

import lombok.Getter;

@Getter
public class PostRequestDto {
    private String title;
    private String content;
    private Double latitude;
    private Double longitude;
}
