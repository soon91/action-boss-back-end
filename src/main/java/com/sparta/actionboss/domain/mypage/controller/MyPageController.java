package com.sparta.actionboss.domain.mypage.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
public class MyPageController {
    //이메일 없는 사람이 확인하고 등록
    //null인 것만 이메일 넣어주기
}
