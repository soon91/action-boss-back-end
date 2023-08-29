package com.sparta.actionboss.domain.search.dto;

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