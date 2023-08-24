package com.sparta.actionboss.domain.search.service;

import com.sparta.actionboss.domain.post.entity.Post;
import com.sparta.actionboss.domain.search.dto.SearchPostListAndTotalPageResponseDto;
import com.sparta.actionboss.domain.search.dto.SearchResponseDto;
import com.sparta.actionboss.domain.search.repository.SearchRepository;
import com.sparta.actionboss.global.exception.SearchException;
import com.sparta.actionboss.global.exception.errorcode.ClientErrorCode;
import com.sparta.actionboss.global.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.sparta.actionboss.global.response.SuccessMessage.SEARCH_SUCCESS;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchService {

    private final SearchRepository searchRepository;

    public CommonResponse<SearchResponseDto> searchPostList(String search) {

        if (search == null) {
            throw new SearchException(ClientErrorCode.SEARCH_NULL);
        }

        Post searchPost = searchRepository.findByAddressContaining(search);

        return new CommonResponse<>(SEARCH_SUCCESS, new SearchResponseDto(searchPost.getLatitude(), searchPost.getLongitude()));
    }
}
