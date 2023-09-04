package com.sparta.actionboss.domain.mypage.controller;

import com.sparta.actionboss.domain.auth.dto.ReissueTokenResponseDto;
import com.sparta.actionboss.domain.mypage.dto.*;
import com.sparta.actionboss.domain.mypage.service.MyPageService;
import com.sparta.actionboss.global.response.CommonResponse;
import com.sparta.actionboss.global.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
public class MyPageController {

    private final MyPageService myPageService;

    //마이페이지 조회
    @GetMapping("/getUserInfo")
    public ResponseEntity<CommonResponse<MyPageInfoResponseDto>> getUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return new ResponseEntity<>(myPageService.getUserInfo(userDetails.getUser()), HttpStatus.OK);
    }

    //이메일 등록
    @PatchMapping("/updateEmail")
    public ResponseEntity<CommonResponse> updateEmail(@RequestBody @Valid UpdateEmailRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
       return new ResponseEntity<>(myPageService.updateEmail(requestDto, userDetails.getUser()), HttpStatus.CREATED);
    }

    //회원탈퇴
    @DeleteMapping("/deleteAccount")
    public ResponseEntity<CommonResponse> deleteAccount(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return new ResponseEntity<>(myPageService.deleteAccount(userDetails.getUser()), HttpStatus.OK);
    }

    //닉네임 변경
    @PatchMapping("/updateNickname")
    public ResponseEntity<CommonResponse<UpdateNicknameResponseDto>> updateNickname(@RequestBody @Valid UpdateNicknameRequestDto requestDto, HttpServletResponse response, @AuthenticationPrincipal UserDetailsImpl userDetails){
        CommonResponse<UpdateNicknameResponseDto> commonResponse = myPageService.updateNickname(requestDto, userDetails.getUser(), response);
        return new ResponseEntity<>(new CommonResponse<>(commonResponse.getMsg()), HttpStatus.CREATED);
    }

    //비밀번호 변경
    @PatchMapping("/updatePassword")
    public ResponseEntity<CommonResponse> updatePassword(@RequestBody @Valid UpdatePasswordRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return new ResponseEntity<>(myPageService.updatePassword(requestDto, userDetails.getUser()), HttpStatus.CREATED);
    }

}
