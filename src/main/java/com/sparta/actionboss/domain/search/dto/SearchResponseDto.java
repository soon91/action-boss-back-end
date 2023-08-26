package com.sparta.actionboss.domain.search.dto;

import com.sparta.actionboss.domain.post.entity.Post;
import lombok.Getter;

@Getter
public class SearchResponseDto {
    private Double latitude;
    private Double longitude;

    public SearchResponseDto(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}