package com.sparta.actionboss.domain.auth.dto;

import lombok.Getter;

@Getter
public class userResponseDto {
    private String msg;

    public userResponseDto(String msg){
        this.msg = msg;
    }

}
