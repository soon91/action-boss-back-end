package com.sparta.actionboss.global.advice;

import com.sparta.actionboss.global.exception.*;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Slf4j(topic = "error")
public class CustomExceptionHandler {

    @ExceptionHandler(SignupException.class)
    public ResponseEntity<?> signupExceptionHandler(SignupException e) {
        return ResponseEntity
                .status(e.getErrorCode().getStatusCode())
                .body(new ErrorResponse(e.getErrorCode().getMsg()));
    }

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<?> loginExceptionHandler(LoginException e) {
        return ResponseEntity
                .status(e.getErrorCode().getStatusCode())
                .body(new ErrorResponse(e.getErrorCode().getMsg()));
    }

    @ExceptionHandler(PostException.class)
    public ResponseEntity<?> postExceptionHandler(PostException e) {
        return ResponseEntity
                .status(e.getErrorCode().getStatusCode())
                .body(new ErrorResponse(e.getErrorCode().getMsg()));
    }

}
