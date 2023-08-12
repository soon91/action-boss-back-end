package com.sparta.actionboss.domain.auth.service;

import com.sparta.actionboss.domain.auth.dto.*;
import com.sparta.actionboss.domain.auth.entity.Email;
import com.sparta.actionboss.domain.auth.entity.User;
import com.sparta.actionboss.domain.auth.entity.UserRoleEnum;
import com.sparta.actionboss.domain.auth.repository.EmailRepository;
import com.sparta.actionboss.domain.auth.repository.UserRepository;
import com.sparta.actionboss.domain.auth.util.EmailUtil;
import com.sparta.actionboss.global.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailRepository emailRepository;
    private final EmailUtil emailUtil;
    private final JwtUtil jwtUtil;


    // ADMIN_TOKEN
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    //회원가입
    public void signup(SignupRequestDto requestDto) {
        String nickname = requestDto.getNickname();
        String email = requestDto.getEmail();
        String password = passwordEncoder.encode(requestDto.getPassword());

        //닉네임 중복확인
        if(checkNickname(nickname)){
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }

        long emailId = checkEmailSuccessKey(requestDto.getEmail(), requestDto.getSuccessKey());
        emailRepository.deleteById(emailId);

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new IllegalArgumentException("관리자 암호가 일치하지 않습니다.");
            }
            role = UserRoleEnum.ADMIN;
        }

        // 사용자 등록
        User user = new User(nickname, password, email, role);
        userRepository.save(user);
    }

    //로그인
    public LoginResponseDto login(LoginRequestDto requestDto){
        User user = userRepository.findByEmail(requestDto.getEmail()).orElseThrow(() ->
                new IllegalArgumentException("가입되지 않은 이메일입니다."));
        if(!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())){
            throw new IllegalArgumentException("잘못된 비밀번호 입니다.");
        }
        String accessToken = jwtUtil.createToken(user.getEmail(), user.getRole());
        TokenDto tokenDto = new TokenDto(accessToken);
        return new LoginResponseDto(tokenDto.getAccessToken());

    }

    //닉네임 중복확인
    @Transactional(readOnly = true)
    public userResponseDto checkNickname(CheckNicknameRequestDto requestDto) {
        String nickname = requestDto.getNickname();
        if (checkNickname(nickname)) {
            return new userResponseDto("이미 존재하는 닉네임입니다.");
        } else {
            return new userResponseDto("사용 가능한 닉네임입니다.");
        }
    }

    @Transactional
    public userResponseDto sendEmail(SendEmailRequestDto requestDto) {
        Optional<Email> email = emailRepository.findByEmail(requestDto.getEmail());

        userResponseDto response = new userResponseDto("이메일 인증 코드을 보냈습니다. 확인해 주시길 바랍니다.");

        String successKey = emailUtil.makeRandomNumber();

        emailUtil.sendEmail(requestDto.getEmail(), successKey);
        if (email.isEmpty()) {
            emailRepository.save(Email.builder()
                    .email(requestDto.getEmail())
                    .successKey(successKey)
                    .build());
            return response;
        }
        //위에서 null 체크
        email.get().changeSuccessKey(successKey);

        return response;
    }

    @Transactional
    public userResponseDto checkEmail(CheckEmailRequestDto requestDto) {
        checkEmailSuccessKey(requestDto.getEmail(), requestDto.getSuccessKey());
        return new userResponseDto("이메일 인증이 완료되었습니다.");
    }

    private long checkEmailSuccessKey(String requestEmail, String successKey) {
        Email email = emailRepository.findByEmail(requestEmail).orElseThrow(
                ()-> new IllegalArgumentException("존재하지 않는 이메일입니다."));
        if(!email.getSuccessKey().equals(successKey)){
            throw new IllegalArgumentException("이메일 인증에 실패하였습니다.");
        }
        return email.getId();
    }

    private boolean checkNickname(String nickname){
        Optional<User> checkNickname = userRepository.findByNickname(nickname);
        if(checkNickname.isPresent()){
            return true;
        } else {
            return false;
        }
    }
}
