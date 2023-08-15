package com.sparta.actionboss.domain.post.controller;


import com.sparta.actionboss.domain.post.dto.*;
import com.sparta.actionboss.domain.post.entity.Post;
import com.sparta.actionboss.domain.post.service.PostService;
import com.sparta.actionboss.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;


    // 민원글 작성
    @PostMapping("")
    public ResponseEntity<CreatePostResponseDto> createPost(
            @RequestPart(name = "post") PostRequestDto postRequestDto,
            @RequestPart(value = "images") List<MultipartFile> images,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) throws IOException {
        return postService.createPost(
                postRequestDto,
                images,
                userDetails.getUser()
        );
    }

    // 민원글 상세 조회 (postId)
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> getPost(
            @PathVariable Long postId
    ) {
        return postService.getPost(postId);
    }


    // 민원글 수정
    @PutMapping("/{postId}")
    public ResponseEntity<UpdatePostResponseDto> updatePost(
            @PathVariable Long postId,
            @RequestBody PostRequestDto postRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return postService.updatePost(
                postId,
                postRequestDto,
                userDetails.getUser()
        );
//        return new ResponseEntity<>(postService.updatePost(postId, postRequestDto, userDetails.getUser()));
    }


    // 민원글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<DeleteResponseDto> deletePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return postService.deletePost(
                postId,
                userDetails.getUser()
        );
    }
}
