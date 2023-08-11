package com.sparta.actionboss.domain.post.controller;

import com.sparta.actionboss.domain.post.dto.PostListAndTotalPageResponseDto;
import com.sparta.actionboss.domain.post.service.PostGetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostGetController {

    private final PostGetService postGetService;

    @GetMapping("/posts")
    public PostListAndTotalPageResponseDto getPostList(
            @RequestParam Integer page,
            @RequestParam Integer size
//            @RequestParam String sortBy
    ) {
        PostListAndTotalPageResponseDto postListAndTotalPageResponseDto = postGetService.getPostList(page, size);
        return postListAndTotalPageResponseDto;
    }
}
