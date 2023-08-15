package com.sparta.actionboss.global.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ClientErrorCode {
    DUPLICATE_NICKNAME(HttpStatus.BAD_REQUEST, "이미 존재하는 닉네임입니다."),
    EMAIL_AUTHENTICATION_FAILED(HttpStatus.BAD_REQUEST, "이메일 인증에 실패하였습니다."),
    EMAIL_SENDING_FAILED(HttpStatus.BAD_REQUEST, "이메일 인증 코드를 보내지 못했습니다."),
    INVALID_ADMIN_TOKEN(HttpStatus.BAD_REQUEST, "관리자 암호가 일치하지 않습니다."),
    SIGNUP_FAILED(HttpStatus.BAD_REQUEST, "회원가입에 실패하였습니다."),

    NO_ACCOUNT(HttpStatus.UNAUTHORIZED, "가입되지 않은 이메일입니다."),
    INVALID_PASSWORDS(HttpStatus.UNAUTHORIZED, "잘못된 비밀번호 입니다.")
    ;

    private final HttpStatus statusCode;
    private final String msg;
}
