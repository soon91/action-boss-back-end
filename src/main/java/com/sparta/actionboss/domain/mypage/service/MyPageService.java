package com.sparta.actionboss.domain.mypage.service;

import com.sparta.actionboss.domain.auth.entity.User;
import com.sparta.actionboss.domain.auth.repository.UserRepository;
import com.sparta.actionboss.domain.mypage.dto.UpdateEmailRequestDto;
import com.sparta.actionboss.global.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.sparta.actionboss.global.response.SuccessMessage.*;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final UserRepository userRepository;

    //이메일 없는 사람을 확인하고 등록
    public CommonResponse updateEmail(UpdateEmailRequestDto requestDto, User user) {
        //비어있다면 새로운 이메일 넣어주기
        if(user.getEmail() == null){
            user.updateEmail(requestDto);
            userRepository.save(user);
        }
        return new CommonResponse(UPDATE_EMAIL);
    }
}
