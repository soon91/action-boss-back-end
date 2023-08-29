package com.sparta.actionboss.domain.mypage.controller;

import com.sparta.actionboss.domain.mypage.dto.UpdateEmailRequestDto;
import com.sparta.actionboss.domain.mypage.service.MyPageService;
import com.sparta.actionboss.global.response.CommonResponse;
import com.sparta.actionboss.global.security.UserDetailsImpl;
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


    //이메일 등록
    @PatchMapping("/updateEmail")
    public ResponseEntity<CommonResponse> updateEmail(@RequestBody @Valid UpdateEmailRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
       return new ResponseEntity<>(myPageService.updateEmail(requestDto, userDetails.getUser()), HttpStatus.CREATED);
    }
}
