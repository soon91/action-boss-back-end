package com.sparta.actionboss.domain.post.controller;


import com.sparta.actionboss.domain.post.dto.DeleteResponseDto;
import com.sparta.actionboss.domain.post.dto.PostRequestDto;
import com.sparta.actionboss.domain.post.dto.PostResponseDto;
import com.sparta.actionboss.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("")
    public ResponseEntity<PostResponseDto> createPost(
            @RequestPart(name = "post") PostRequestDto postRequestDto,
            @RequestPart("images") List<MultipartFile> images
    ) throws IOException {
        return postService.createPost(postRequestDto, images);
    }
    /*
    public ResponseEntity<Long> createPost(
		@RequestParam(value = "title") String title,
		@RequestParam(value = "content") String content,
		@RequestParam(value = "latitude") Double latitude,
		@RequestParam(value = "longitude") Double longitude

		// (required = false)
    )
     */

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long postId) {
        return postService.getPost(postId);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostResponseDto> updatePost(@PathVariable Long postId, @RequestBody PostRequestDto postRequestDto) {
        return postService.updatePost(postId, postRequestDto);
    }

    @DeleteMapping("{postId}")
    public ResponseEntity<DeleteResponseDto> deletePost(@PathVariable Long postId) {
        return postService.deletePost(postId);
    }
}
