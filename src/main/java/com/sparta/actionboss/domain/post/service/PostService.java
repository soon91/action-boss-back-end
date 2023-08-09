package com.sparta.actionboss.domain.post.service;

import com.sparta.actionboss.domain.post.dto.DeleteResponseDto;
import com.sparta.actionboss.domain.post.dto.PostRequestDto;
import com.sparta.actionboss.domain.post.dto.PostResponseDto;
import com.sparta.actionboss.domain.post.entity.Post;
import com.sparta.actionboss.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {
    private final com.sparta.actionboss.domain.post.service.S3Uploader s3Uploader;
    private final PostRepository postRepository;

    private static final int MAXIMUM_IMAGES = 3;

    public ResponseEntity<PostResponseDto> createPost(PostRequestDto postRequestDto, List<MultipartFile> images) throws IOException {
        if (!images.isEmpty()) {
            if (images.size() > MAXIMUM_IMAGES) {
                throw new IllegalArgumentException("최대 " + MAXIMUM_IMAGES + "장의 이미지만 업로드할 수 있습니다.");
            }
            // 요청별로 폴더생성 -> 저장
            String directoryPath = "images/" + UUID.randomUUID().toString();

            List<String> imageURLs = s3Uploader.upload(images, directoryPath);
            Post post = new Post(postRequestDto, imageURLs);
            postRepository.save(post);
            return ResponseEntity.ok(new PostResponseDto(post));
        } else {
            throw new IllegalArgumentException("사진을 1장 이상 업로드 해주세요.");
        }
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

