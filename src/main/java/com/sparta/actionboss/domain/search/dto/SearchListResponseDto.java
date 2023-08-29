package com.sparta.actionboss.domain.search.dto;

import lombok.Getter;

@Getter
public class SearchListResponseDto {
    private String address;

    public SearchListResponseDto(String address) {
        this.address = address;
    }
}