package com.sparta.actionboss.domain.done.controller;

import com.sparta.actionboss.domain.done.dto.PostDoneResponseDto;
import com.sparta.actionboss.domain.done.service.PostDoneService;
import com.sparta.actionboss.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/")
public class PostDoneController {
    private final PostDoneService postDoneService;

    @PostMapping("{postId}/done")
    public ResponseEntity<?> createLike(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postDoneService.createLike(postId, userDetails.getUser());
    }
}
