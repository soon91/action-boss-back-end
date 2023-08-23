package com.sparta.actionboss.global.security;


import com.sparta.actionboss.domain.auth.entity.UserRoleEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j(topic = "JwtUtil")
@Component
@RequiredArgsConstructor
public class JwtUtil {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTHORIZATION_KEY = "auth";
    public static final String AUTHORIZATION_ACCESS = "Access";
    public static final String AUTHORIZATION_REFRESH = "Refresh";
    public static final String BEARER_PREFIX = "Bearer ";

    @Value("${jwt.secret.key.access}")
    private String accessTokenSecretKey;
    @Value("${jwt.secret.key.refresh}")
    private String refreshTokenSecretKey;
    private Key accessTokenKey;
    private Key refreshTokenKey;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] accessTokenBytes = Base64.getDecoder().decode(accessTokenSecretKey);
        accessTokenKey = Keys.hmacShaKeyFor(accessTokenBytes);

        byte[] refreshTokenBytes = Base64.getDecoder().decode(refreshTokenSecretKey);
        refreshTokenKey = Keys.hmacShaKeyFor(refreshTokenBytes);
    }

    // 토큰 생성
    public String createAccessToken(String email, UserRoleEnum role) {
        Date date = new Date();

//        long TOKEN_TIME = 60 * 60 * 1000L; // 60분
        long TOKEN_TIME = 1 * 60 * 1000L; //     1분(test용)

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(email)
                        .claim(AUTHORIZATION_KEY, role)
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME))
                        .setIssuedAt(date)
                        .signWith(accessTokenKey, signatureAlgorithm)
                        .compact();
    }

    public String createRefreshToken() {
        Date date = new Date();

//        long TOKEN_TIME = 7 * 24 * 60 * 60 * 1000L;   //5분
        long TOKEN_TIME = 5 * 60 * 1000L; //     1분(test용)


        return BEARER_PREFIX +
                Jwts.builder()
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME))
                        .setIssuedAt(date)
                        .signWith(refreshTokenKey, signatureAlgorithm)
                        .compact();
    }

    public String getJwtFromHeader(HttpServletRequest request, String tokenType) {
        if(tokenType.equals("Access")){
            String bearerToken = request.getHeader(AUTHORIZATION_ACCESS);
            if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
                return bearerToken.substring(7);
            }
        }else {
            String bearerToken = request.getHeader(AUTHORIZATION_REFRESH);
            if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
                return bearerToken.substring(7);
            }
        }
        return null;
    }



    // 토큰 검증
    public boolean validateAccessToken(String token, HttpServletRequest request) {
        try {
            String headerValue = request.getHeader(AUTHORIZATION_ACCESS);
            log.info("Access token header value: {}", headerValue);
            Jwts.parserBuilder().setSigningKey(accessTokenKey).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
            throw new IllegalArgumentException("유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
            throw new IllegalArgumentException("만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
            throw new IllegalArgumentException("지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
            throw new IllegalArgumentException("잘못된 JWT 토큰 입니다.");
        }
    }

    public boolean validateRefreshToken(String token, HttpServletRequest request) {
        try {
            String headerValue = request.getHeader(AUTHORIZATION_REFRESH);
            log.info("Refresh token header value: {}", headerValue);
            Jwts.parserBuilder().setSigningKey(refreshTokenKey).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
            throw new IllegalArgumentException("유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
            throw new IllegalArgumentException("만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
            throw new IllegalArgumentException("지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
            throw new IllegalArgumentException("잘못된 JWT 토큰 입니다.");
        }
    }


    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(accessTokenKey).build().parseClaimsJws(token).getBody();
    }

    public String getEmailFromToken(String token){
        return Jwts.parserBuilder().setSigningKey(refreshTokenKey).build().parseClaimsJws(token).getBody().getSubject();
    }
}
