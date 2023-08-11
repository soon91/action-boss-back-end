package com.sparta.actionboss.domain.post.service;

import com.sparta.actionboss.domain.post.dto.PostListAndTotalPageResponseDto;
import com.sparta.actionboss.domain.post.dto.PostListResponseDto;
import com.sparta.actionboss.domain.post.entity.Post;
import com.sparta.actionboss.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostGetService {

    private final PostRepository postRepository;

    public PostListAndTotalPageResponseDto getPostList(Integer page, Integer limit) {
        Sort.Direction direction = Sort.Direction.DESC;
        Sort sort = Sort.by(direction, "modifiedAt");

        Pageable pageable = PageRequest.of(page, limit, sort);
        Page<Post> post = postRepository.findAll(pageable);

        List<PostListResponseDto> postListResponseDtos = post.stream()
                .map(a -> {
                    // TODO 좋아요갯수 구하는 로직

                    return new PostListResponseDto(
                            a.getPostId(),
                            a.getTitle(),
                            // TODO 이미지, 좋아요갯수
                            a.getUser().getNickname(),
                            a.getLatitude(),
                            a.getLongitude(),
                            a.getAddress(),
                            a.getImageUrls().get(0)

                    );
                })
                .collect(Collectors.toList());


        return new PostListAndTotalPageResponseDto(postListResponseDtos, post.getTotalPages());
    }
}
