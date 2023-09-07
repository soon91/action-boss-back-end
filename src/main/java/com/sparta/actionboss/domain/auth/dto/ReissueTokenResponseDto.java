package com.sparta.actionboss.domain.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReissueTokenResponseDto {
    private String accessToken;
}
