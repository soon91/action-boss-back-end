package com.sparta.actionboss.domain.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.actionboss.domain.auth.dto.*;
import com.sparta.actionboss.domain.auth.service.KakaoService;
import com.sparta.actionboss.domain.auth.service.UserService;
import com.sparta.actionboss.global.response.CommonResponse;
import com.sparta.actionboss.global.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;
    private final KakaoService kakaoService;

    @PostMapping("/signup")
    public ResponseEntity<CommonResponse> signup(@RequestBody @Valid SignupRequestDto requestDto){
        return new ResponseEntity<>(userService.signup(requestDto), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<CommonResponse<LoginResponseDto>> login(@RequestBody @Valid LoginRequestDto requestDto, HttpServletResponse response){
        CommonResponse<LoginResponseDto> commonResponse = userService.login(requestDto);
        LoginResponseDto responseDto = commonResponse.getData();
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, responseDto.getAccessToken());
        return new ResponseEntity<>(new CommonResponse<>(commonResponse.getMsg()), HttpStatus.OK);
    }

    @PostMapping("/kakao")
    public ResponseEntity<CommonResponse<LoginResponseDto>> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        CommonResponse<LoginResponseDto> commonResponse = kakaoService.kakaoLogin(code);
        LoginResponseDto responseDto = commonResponse.getData();
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, responseDto.getAccessToken());
        return new ResponseEntity<>(new CommonResponse<>(commonResponse.getMsg()), HttpStatus.OK);
    }

    @PostMapping("/signup/nicknameCheck")
    public ResponseEntity<CommonResponse> checkNickname(@RequestBody @Valid CheckNicknameRequestDto requestDto){
        return new ResponseEntity<>(userService.checkNickname(requestDto), HttpStatus.CREATED);
    }

    @PostMapping("/signup/emailSend")
    public ResponseEntity<CommonResponse> sendEmail(@RequestBody @Valid SendEmailRequestDto requestDto){
        return new ResponseEntity<>(userService.sendEmail(requestDto), HttpStatus.CREATED);
    }

    @PostMapping("/signup/emailCheck")
    public ResponseEntity<CommonResponse> checkEmail(@RequestBody @Valid CheckEmailRequestDto requestDto){
        return new ResponseEntity<>(userService.checkEmail(requestDto), HttpStatus.CREATED);
    }
}
