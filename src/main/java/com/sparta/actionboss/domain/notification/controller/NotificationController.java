package com.sparta.actionboss.domain.notification.controller;


import com.sparta.actionboss.domain.notification.service.NotificationService;
import com.sparta.actionboss.global.response.CommonResponse;
import com.sparta.actionboss.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return notificationService.subscribe(userDetails.getUser().getUserId());
    }

    @GetMapping("/unsubscribe")
    public void unsubscribe(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        notificationService.unsubscribe(userDetails.getUser().getUserId());
    }

    @GetMapping("")
    public ResponseEntity<CommonResponse> getNotification(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return new ResponseEntity<>(notificationService.getNotification(
                userDetails.getUser()),
                HttpStatus.OK
        );
    }

    @PutMapping("/read/{notificationId}")
    public ResponseEntity<CommonResponse> changeReadStatus(
            @PathVariable Long notificationId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return new ResponseEntity<>(notificationService.changeReadStatus(
                notificationId, userDetails.getUser()),
                HttpStatus.OK
        );
    }
}
