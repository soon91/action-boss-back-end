package com.sparta.actionboss.domain.post.controller;

import com.sparta.actionboss.domain.post.dto.MapListResponseDto;
import com.sparta.actionboss.domain.post.dto.PostListAndTotalPageResponseDto;
import com.sparta.actionboss.domain.post.dto.PostModalResponseDto;
//import com.sparta.actionboss.domain.post.service.PostGetService;
import com.sparta.actionboss.domain.post.service.PostGetService;
import com.sparta.actionboss.global.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostGetController {

    private final PostGetService postGetService;

    @GetMapping("/main")
    public ResponseEntity<CommonResponse<PostListAndTotalPageResponseDto>> getPostList(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam Integer size,
            @RequestParam String sort,
            @RequestParam boolean isdone,
            @RequestParam Double northlatitude,
            @RequestParam Double eastlongitude,
            @RequestParam Double southlatitude,
            @RequestParam Double westlongitude
    ) {
        return new ResponseEntity<>(postGetService.getPostList
                (page, size, sort, isdone, northlatitude, eastlongitude, southlatitude, westlongitude), HttpStatus.OK);
    }

    @GetMapping("/main/{postId}")
    public ResponseEntity<CommonResponse<PostModalResponseDto>> getSelectPost(@PathVariable Long postId) {
        return new ResponseEntity<>(postGetService.getModalPost(postId), HttpStatus.OK);
    }

    @GetMapping("/main/map")
    public ResponseEntity<CommonResponse<List<MapListResponseDto>>> getMapList(
            @RequestParam boolean isdone,
            @RequestParam Double northlatitude,
            @RequestParam Double eastlongitude,
            @RequestParam Double southlatitude,
            @RequestParam Double westlongitude
    ) {
        return new ResponseEntity<>(postGetService.getMapList(isdone, northlatitude, eastlongitude, southlatitude, westlongitude), HttpStatus.OK);
    }
}
