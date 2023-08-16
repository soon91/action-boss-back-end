package com.sparta.actionboss.domain.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class SignupRequestDto {
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;
    @Pattern(regexp = "^[a-zA-Z0-9]{8,15}$")
    private String password;
    private String nickname;
    private String successKey;
    private boolean admin = false;
    private String adminToken = "";
}
