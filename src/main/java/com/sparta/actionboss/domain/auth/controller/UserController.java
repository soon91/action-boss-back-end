package com.sparta.actionboss.domain.auth.controller;

import com.sparta.actionboss.domain.auth.dto.*;
import com.sparta.actionboss.domain.auth.service.MailSendServiceImpl;
import com.sparta.actionboss.domain.auth.service.UserService;
import com.sparta.actionboss.global.security.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public userResponseDto signup(@RequestBody @Valid SignupRequestDto requestDto){
        userService.signup(requestDto);
        return new userResponseDto("회원가입에 성공하였습니다.");
    }

    @PostMapping("/login")
    public userResponseDto login(@RequestBody LoginRequestDto requestDto, HttpServletResponse response){
        LoginResponseDto responseDto = userService.login(requestDto);
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, responseDto.getAccessToken());
        return new userResponseDto("로그인에 성공하였습니다.");
    }

    @PostMapping("/signup/nicknameCheck")
    public userResponseDto nicknameCheck(@RequestBody NicknameCheckRequestDto requestDto){
        return userService.nicknameCheck(requestDto);
    }

}
