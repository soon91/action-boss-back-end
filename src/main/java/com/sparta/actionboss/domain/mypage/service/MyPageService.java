package com.sparta.actionboss.domain.mypage.service;

import com.sparta.actionboss.domain.auth.entity.User;
import com.sparta.actionboss.domain.auth.repository.UserRepository;
import com.sparta.actionboss.domain.mypage.dto.UpdateEmailRequestDto;
import com.sparta.actionboss.domain.mypage.dto.UpdateNicknameRequestDto;
import com.sparta.actionboss.domain.mypage.dto.UpdatePasswordRequestDto;
import com.sparta.actionboss.global.exception.SignupException;
import com.sparta.actionboss.global.exception.errorcode.ClientErrorCode;
import com.sparta.actionboss.global.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.sparta.actionboss.global.response.SuccessMessage.*;

@Slf4j(topic = "mypage service")
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
            return new CommonResponse(UPDATE_EMAIL);
        } else {
            throw new SignupException(ClientErrorCode.DUPLICATE_EMAIL);
        }
    }

    //회원탈퇴
    @Transactional
    public CommonResponse deleteAccount(User user) {
        User currentUser = userRepository.findByEmail(user.getEmail()).orElseThrow(
                ()-> new SignupException(ClientErrorCode.NO_ACCOUNT));
        userRepository.delete(currentUser);
        return new CommonResponse(DELETE_ACCOUNT);
    }

    //닉네임 수정
    @Transactional
    public CommonResponse updateNickname(UpdateNicknameRequestDto requestDto, User user) {
        String newNickname = requestDto.getNickname();

        Optional<User> existingUserWithNewNickname = userRepository.findByNickname(newNickname);
        if (existingUserWithNewNickname.isPresent()) {
            throw new SignupException(ClientErrorCode.DUPLICATE_NICKNAME);
        }

//        if(userRepository.findByNickname(newNickname).isPresent()){
//            throw new SignupException(ClientErrorCode.DUPLICATE_NICKNAME);
//        }

        user.updateNickname(newNickname);
        userRepository.save(user);


        return new CommonResponse(UPDATE_NICKNAME);
    }

    //비밀번호 변경
    @Transactional
    public CommonResponse updatePassword(UpdatePasswordRequestDto requestDto, User user) {
        String newPassword = requestDto.getPassword();
        return null;
    }


}
