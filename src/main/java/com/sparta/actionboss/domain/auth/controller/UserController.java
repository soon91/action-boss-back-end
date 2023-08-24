package com.sparta.actionboss.domain.auth.controller;

import com.sparta.actionboss.domain.auth.dto.*;
import com.sparta.actionboss.domain.auth.service.UserService;
import com.sparta.actionboss.global.response.CommonResponse;
import com.sparta.actionboss.global.security.JwtUtil;
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

    @PostMapping("/signup")
    public ResponseEntity<CommonResponse> signup(@RequestBody @Valid SignupRequestDto requestDto){
        return new ResponseEntity<>(userService.signup(requestDto), HttpStatus.CREATED);
    }

//    TODO : token이 안넘어 갈 경우를 위해 남겨둠
//    @PostMapping("/login")
//    public userResponseDto login(@RequestBody LoginRequestDto requestDto, HttpServletResponse response){
//        LoginResponseDto responseDto = userService.login(requestDto);
//        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, responseDto.getAccessToken());
//        return new userResponseDto("로그인에 성공하였습니다.");
//    }

    @PostMapping("/login")
    public ResponseEntity<CommonResponse<LoginResponseDto>> login(@RequestBody LoginRequestDto requestDto, HttpServletResponse response){
        CommonResponse<LoginResponseDto> commonResponse = userService.login(requestDto);
        LoginResponseDto responseDto = commonResponse.getData();
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, responseDto.getAccessToken());
        return new ResponseEntity<>(new CommonResponse<>(commonResponse.getMsg()), HttpStatus.OK);
    }

    @PostMapping("/signup/nicknameCheck")
    public ResponseEntity<CommonResponse> checkNickname(@RequestBody CheckNicknameRequestDto requestDto){
        return new ResponseEntity<>(userService.checkNickname(requestDto), HttpStatus.CREATED);
    }

    @PostMapping("/signup/emailSend")
    public ResponseEntity<CommonResponse> sendEmail(@RequestBody SendEmailRequestDto requestDto){
        return new ResponseEntity<>(userService.sendEmail(requestDto), HttpStatus.CREATED);
    }

    @PostMapping("/signup/emailCheck")
    public ResponseEntity<CommonResponse> checkEmail(@RequestBody CheckEmailRequestDto requestDto){
        return new ResponseEntity<>(userService.checkEmail(requestDto), HttpStatus.CREATED);
    }
}
