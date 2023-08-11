package com.sparta.actionboss.domain.auth.dto;

import lombok.Getter;

@Getter
public class LoginResponseDto {
    private String accessToken;

    public LoginResponseDto(String accessToken){
        this.accessToken = accessToken;
    }
}
