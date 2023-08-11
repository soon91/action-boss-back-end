package com.sparta.actionboss.domain.post.controller;

import com.sparta.actionboss.domain.post.dto.PostListAndTotalPageResponseDto;
import com.sparta.actionboss.domain.post.dto.PostModalResponseDto;
import com.sparta.actionboss.domain.post.service.PostGetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostGetController {

    private final PostGetService postGetService;

    @GetMapping("/main")
    public PostListAndTotalPageResponseDto getPostList(
            @RequestParam Integer page,
            @RequestParam Integer size
//            @RequestParam String sortBy
    ) {
        PostListAndTotalPageResponseDto postListAndTotalPageResponseDto = postGetService.getPostList(page, size);
        return postListAndTotalPageResponseDto;
    }

    @GetMapping("/main/{postId}")
    public PostModalResponseDto getSelectPost(@PathVariable Long postId) {
        PostModalResponseDto postModalResopnseDto = postGetService.getModalPost(postId);
        return postModalResopnseDto;
    }
}
