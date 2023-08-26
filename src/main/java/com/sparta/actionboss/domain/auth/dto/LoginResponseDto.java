package com.sparta.actionboss.domain.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginResponseDto {
    private String accessToken;

    public LoginResponseDto(String accessToken){
        this.accessToken = accessToken;
    }
}
