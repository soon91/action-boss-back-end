package com.sparta.actionboss.global.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.actionboss.global.exception.ErrorResponse;
import com.sparta.actionboss.global.exception.errorcode.ClientErrorCode;
import com.sparta.actionboss.global.util.JwtUtil;
import com.sparta.actionboss.global.security.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.sparta.actionboss.global.util.JwtUtil.*;

@Slf4j(topic = "JWT 검증 및 인가")
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

//    @Override
//    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
//
//        String accessTokenValue = jwtUtil.getJwtFromHeader(req, AUTHORIZATION_ACCESS);
//
//        log.info("Access token value: {}", accessTokenValue);
//
//        if (StringUtils.hasText(accessTokenValue)) {
//
//            if (!jwtUtil.validateAccessToken(accessTokenValue)) {
//                log.error("Token Error");
//                return;
//            }
//
//            Claims info = jwtUtil.getUserInfoFromAccessToken(accessTokenValue);
//
//            try {
//                setAuthentication(info.getSubject());
//            } catch (Exception e) {
//                log.error(e.getMessage());
//                return;
//            }
//        }
//
//        filterChain.doFilter(req, res);
//    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        String accessTokenValue = jwtUtil.getJwtFromHeader(req, AUTHORIZATION_ACCESS);

        log.info("Access token value: {}", accessTokenValue);

        if (StringUtils.hasText(accessTokenValue)) {

            if (!jwtUtil.validateAccessToken(accessTokenValue)) {
                log.error("Token Error");

                // 유효하지 않은 토큰에 대한 커스텀 응답
                ErrorResponse errorResponse = new ErrorResponse("유효하지 않은 토큰입니다.");
                sendErrorResponse(res, HttpStatus.FORBIDDEN, errorResponse);
                return;
            }

            Claims info = jwtUtil.getUserInfoFromAccessToken(accessTokenValue);

            try {
                setAuthentication(info.getSubject());
            } catch (Exception e) {
                log.error(e.getMessage());

                // 예외에 대한 커스텀 응답
                ErrorResponse errorResponse = new ErrorResponse("서버 오류입니다.");
                sendErrorResponse(res, HttpStatus.INTERNAL_SERVER_ERROR, errorResponse);
                return;
            }
        }

        filterChain.doFilter(req, res);
    }

    private void sendErrorResponse(HttpServletResponse response, HttpStatus httpStatus, ErrorResponse errorResponse) throws IOException {
        response.setStatus(httpStatus.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonErrorResponse = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(jsonErrorResponse);
    }



    // 인증 처리
    public void setAuthentication(String nickname) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(nickname);
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String nickname) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(nickname);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
