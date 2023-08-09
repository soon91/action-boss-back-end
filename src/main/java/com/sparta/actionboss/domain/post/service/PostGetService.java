package com.sparta.actionboss.domain.post.service;

import com.sparta.actionboss.domain.post.dto.PostResponseDto;
import com.sparta.actionboss.domain.post.entity.Post;
import com.sparta.actionboss.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostGetService {

    private final PostRepository postRepository;

    public List<PostResponseDto> getPostList() {
        List<Post> postList = postRepository.findAllByOrderByModifiedAtDesc();
        return postList.stream().map(PostResponseDto::new).toList();
    }
}
