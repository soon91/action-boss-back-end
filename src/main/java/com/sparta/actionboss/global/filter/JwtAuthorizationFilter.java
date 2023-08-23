package com.sparta.actionboss.global.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.actionboss.domain.auth.entity.User;
import com.sparta.actionboss.domain.auth.repository.UserRepository;
import com.sparta.actionboss.global.security.JwtUtil;
import com.sparta.actionboss.global.security.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.sparta.actionboss.global.security.JwtUtil.AUTHORIZATION_ACCESS;
import static com.sparta.actionboss.global.security.JwtUtil.AUTHORIZATION_REFRESH;

@Slf4j(topic = "JWT 검증 및 인가")
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final UserRepository userRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {

        String accessTokenValue = jwtUtil.getJwtFromHeader(req, AUTHORIZATION_ACCESS);
        String refreshTokenValue = jwtUtil.getJwtFromHeader(req, AUTHORIZATION_REFRESH);

        log.info("Access token value: {}", accessTokenValue);
        log.info("Refresh token value: {}", refreshTokenValue);

        if(accessTokenValue!=null){
            Claims claims = jwtUtil.getUserInfoFromToken(accessTokenValue);
            String email = claims.getSubject();
            log.info("Access token email: {}", email);
            setAuthentication(email);
        } else if(refreshTokenValue!=null){
            if ((jwtUtil.validateRefreshToken(refreshTokenValue, req))){
                String email = jwtUtil.getEmailFromToken(refreshTokenValue);
                User user = userRepository.findByEmail(email).orElseThrow(
                        ()-> new IllegalArgumentException("잘못된 이메일입니다."));
                String newAccessTokenValue = jwtUtil.createAccessToken(email, user.getRole());
                log.info("Creating new access token for email: {}", email);
                setAuthentication(email);
                res.setHeader(AUTHORIZATION_ACCESS, newAccessTokenValue);
            } else {
                log.warn("Expired access token received");
                sendExpiredAccessTokenResponse(res);
                return;
            }
        }
        filterChain.doFilter(req, res);
    }

    private void sendExpiredAccessTokenResponse(HttpServletResponse res) throws IOException{
        String responseMessage = "{\n \"msg\" : \"Expired AccessToken\"}";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            res.setContentType(MediaType.APPLICATION_JSON_VALUE);
            res.getWriter().print(responseMessage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        res.setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

    // 인증 처리
    public void setAuthentication(String email) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(email);
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String email) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
