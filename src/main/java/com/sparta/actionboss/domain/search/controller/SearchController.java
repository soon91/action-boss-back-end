package com.sparta.actionboss.domain.search.controller;

import com.sparta.actionboss.domain.search.service.SearchService;
import com.sparta.actionboss.global.response.CommonResponse;
import com.sparta.actionboss.domain.search.dto.SearchPostListAndTotalPageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/search")
    public ResponseEntity<CommonResponse<SearchPostListAndTotalPageResponseDto>> searchPostList(
            @RequestParam Integer page,
            @RequestParam Integer size,
            @RequestParam String sort,
            @RequestParam boolean isdone,
            @RequestParam String search
    ) {
        return new ResponseEntity<>(searchService.searchPostList(page, size, sort, isdone, search), HttpStatus.OK);
    }

}
