package com.sparta.actionboss.domain.auth.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CheckNicknameRequestDto {
    @Pattern(regexp = "^(?!.*\\s)[a-zA-Z0-9가-힣]{2,15}$")
    private String nickname;
}
