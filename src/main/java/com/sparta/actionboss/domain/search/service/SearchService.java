package com.sparta.actionboss.domain.search.service;

import com.sparta.actionboss.domain.post.entity.Post;
import com.sparta.actionboss.domain.post.repository.AgreeRepository;
import com.sparta.actionboss.domain.search.dto.SearchPostListAndTotalPageResponseDto;
import com.sparta.actionboss.domain.search.dto.SearchPostListResponseDto;
import com.sparta.actionboss.domain.search.repository.SearchRepository;
import com.sparta.actionboss.global.exception.SearchException;
import com.sparta.actionboss.global.exception.errorcode.ClientErrorCode;
import com.sparta.actionboss.global.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.sparta.actionboss.global.response.SuccessMessage.SEARCH_SUCCESS;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchService {

    @Value("${aws.bucket.url}")
    private String s3Url;

    private final SearchRepository searchRepository;
    private final AgreeRepository agreeRepository;

    public CommonResponse<SearchPostListAndTotalPageResponseDto> searchPostList(
            Integer page, Integer limit, String sortBy, boolean done, String search) {

        if (search == null) {
            throw new SearchException(ClientErrorCode.SEARCH_NULL);
        }

        Sort.Direction direction = Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy, "createdAt");

        Pageable pageable = PageRequest.of(page, limit, sort);
        Page<Post> searchPost;

        if (done) {
            searchPost = searchRepository.findByDoneIsTrueAndAddressContaining(search, pageable);
        } else {
            searchPost = searchRepository.findByDoneIsFalseAndAddressContaining(search, pageable);
        }

        List<SearchPostListResponseDto> searchPostListResponseDtos = searchPost.stream()
                .map(a -> {
                    int agreeCount = agreeRepository.findByPost(a).size();
                    if (Objects.isNull(agreeCount)) {
                        agreeCount = 0;
                    }

                    String imageUrl = s3Url + "/images/" + a.getImageList().get(0).getFolderName() + "/" + a.getImageList().get(0).getImageName();

                    return new SearchPostListResponseDto(
                            a.getPostId(),
                            a.getTitle(),
                            agreeCount,
                            a.getUser().getNickname(),
                            a.getAddress(),
                            imageUrl
                    );
                })
                .collect(Collectors.toList());

        return new CommonResponse<>(SEARCH_SUCCESS, new SearchPostListAndTotalPageResponseDto<>(searchPostListResponseDtos, searchPost.getTotalPages(), page));
    }
}
