package com.sparta.actionboss.domain.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.actionboss.domain.auth.dto.KakaoUserInfoDto;
import com.sparta.actionboss.domain.auth.dto.LoginResponseDto;
import com.sparta.actionboss.domain.auth.entity.User;
import com.sparta.actionboss.domain.auth.entity.UserRoleEnum;
import com.sparta.actionboss.domain.auth.repository.UserRepository;
import com.sparta.actionboss.global.response.CommonResponse;
import com.sparta.actionboss.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

import static com.sparta.actionboss.global.response.SuccessMessage.LOGIN_SUCCESS;

@Slf4j(topic = "KAKAO Login")
@Service
@RequiredArgsConstructor
public class KakaoService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final JwtUtil jwtUtil;

    @Value("${kakao.client.id}")
    private String kakaoClientId;


    public CommonResponse<LoginResponseDto> kakaoLogin(String code) throws JsonProcessingException {
        // 1. "인가 코드"로 "액세스 토큰" 요청
        String accessToken = getToken(code);

        // 2. 토큰으로 카카오 API 호출 : "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
        KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(accessToken);

        // 3. 필요시에 회원가입
        User kakaoUser = registerKakaoUserIfNeeded(kakaoUserInfo);

        // 4. JWT 토큰 반환
        String createToken = jwtUtil.createToken(kakaoUser.getEmail(), kakaoUser.getRole());
        LoginResponseDto responseDto = new LoginResponseDto(createToken);
        return new CommonResponse(LOGIN_SUCCESS, responseDto);
    }

    private String getToken(String code) throws JsonProcessingException {
        log.info("인가코드 : " + code);
        // 요청 URL 만들기
        URI uri = UriComponentsBuilder
                .fromUriString("https://kauth.kakao.com")
                .path("/oauth/token")
                .encode()
                .build()
                .toUri();

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoClientId);
        body.add("redirect_uri", "http://localhost:8080/api/auth/kakao");
//        body.add("redirect_uri", "http://localhost:3000/oauth/callback");
        body.add("code", code);

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(uri)
                .headers(headers)
                .body(body);

        // HTTP 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(
                requestEntity,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        return jsonNode.get("access_token").asText();
    }

    private KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        log.info("accessToken : " + accessToken);
        // 요청 URL 만들기
        URI uri = UriComponentsBuilder
                .fromUriString("https://kapi.kakao.com")
                .path("/v2/user/me")
                .encode()
                .build()
                .toUri();

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(uri)
                .headers(headers)
                .body(new LinkedMultiValueMap<>());

        // HTTP 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(
                requestEntity,
                String.class
        );

        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        Long id = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("properties")
                .get("nickname").asText();


//        String email = jsonNode.get("kakao_account")
//                .get("email").asText();
//
//        log.info("카카오 사용자 정보: " + id + ", " + nickname + ", " + email);
//        return new KakaoUserInfoDto(id, nickname, email);

        JsonNode kakaoAccountNode = jsonNode.get("kakao_account");
        String email = null;
        if (kakaoAccountNode != null && kakaoAccountNode.has("email")) {
            email = kakaoAccountNode.get("email").asText();
            // 이메일이 제공되는 경우에만 DB에 저장
            log.info("카카오 사용자 정보: " + id + ", " + nickname + ", " + email);
            return new KakaoUserInfoDto(id, nickname, email);
        } else {
            log.info("카카오 사용자 정보: " + id + ", " + nickname);
            return new KakaoUserInfoDto(id, nickname);
        }
    }

    private User registerKakaoUserIfNeeded(KakaoUserInfoDto kakaoUserInfo) {
        log.info("registerKakaoUserIfNeeded 메서드 시작");
        // DB 에 중복된 Kakao Id 가 있는지 확인
        Long kakaoId = kakaoUserInfo.getId();
        User kakaoUser = userRepository.findByKakaoId(kakaoId).orElse(null);


        //kakaoId 없다면
        if (kakaoUser == null) {

            //닉네임 = 닉네임 + _KAKAO + kakaoId
            String nickname = kakaoUserInfo.getNickname() + "_KAKAO" + kakaoId;

            // password: random UUID
            String password = UUID.randomUUID().toString();
            String encodedPassword = passwordEncoder.encode(password);

            String kakaoEmail = kakaoUserInfo.getEmail();
            log.info("카카오 이메일 확인: {}", kakaoEmail);
            // 카카오 이메일 제공 동의를 했다면
            if(kakaoEmail != null){
                User sameEmailUser = userRepository.findByEmail(kakaoEmail).orElse(null);
                if (sameEmailUser != null) {
                    log.info("동일한 이메일을 가진 회원이 이미 존재");
                    //만약에 가입한 회원이라면(회원정보가 이미 있다면)
                    kakaoUser = sameEmailUser;
                    // 기존 회원정보에 카카오 Id 추가
                    kakaoUser = kakaoUser.kakaoIdUpdate(kakaoId);
                } else {
                    log.info("신규 회원가입을 진행");
                    // 신규 회원가입
                    // email: kakao email
                    String email = kakaoUserInfo.getEmail();
                    log.info("신규 회원 이메일: {}", email);

                    kakaoUser = new User(nickname, encodedPassword, email, UserRoleEnum.USER, kakaoId);
                }
                userRepository.save(kakaoUser);
            } else {
                // 카카오 이메일 제공 동의를 하지 않았다면
                log.info("이메일이 없으므로 새로운 유저를 생성");
                kakaoUser = new User(nickname, encodedPassword, UserRoleEnum.USER, kakaoId);
                userRepository.save(kakaoUser);
            }
        }
        log.info("registerKakaoUserIfNeeded 메서드 종료");
        return kakaoUser;
    }
}