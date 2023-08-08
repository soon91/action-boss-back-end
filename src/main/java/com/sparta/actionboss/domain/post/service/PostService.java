package com.sparta.actionboss.domain.post.service;

import com.sparta.actionboss.domain.post.dto.DeleteResponseDto;
import com.sparta.actionboss.domain.post.dto.PostRequestDto;
import com.sparta.actionboss.domain.post.dto.PostResponseDto;
import com.sparta.actionboss.domain.post.entity.Post;
import com.sparta.actionboss.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    private final S3Uploader s3Uploader;
    private final PostRepository postRepository;

    public ResponseEntity<PostResponseDto> createPost(PostRequestDto postRequestDto) {
        Post post = new Post(postRequestDto);
        postRepository.save(post);
        return ResponseEntity.ok(new PostResponseDto(post));
    }

    public ResponseEntity<PostResponseDto> getPost(Long postId) {
        Post post = findPost(postId);
        return ResponseEntity.ok(new PostResponseDto(post));
    }


    public ResponseEntity<PostResponseDto> updatePost(Long postId, PostRequestDto postRequestDto) {
        Post post = findPost(postId);
        post.update(postRequestDto);
        return ResponseEntity.ok(new PostResponseDto(post));
    }


    public ResponseEntity<DeleteResponseDto> deletePost(Long postId) {
        Post post = findPost(postId);
        postRepository.delete(post);
        return ResponseEntity.ok(new DeleteResponseDto());
    }


    public Post findPost(Long postId) {
        return postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
    }

}

