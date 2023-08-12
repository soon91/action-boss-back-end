package com.sparta.actionboss.domain.post.service;

import com.sparta.actionboss.domain.auth.entity.User;
import com.sparta.actionboss.domain.auth.entity.UserRoleEnum;
import com.sparta.actionboss.domain.auth.repository.UserRepository;
import com.sparta.actionboss.domain.post.dto.DeleteResponseDto;
import com.sparta.actionboss.domain.post.dto.PostRequestDto;
import com.sparta.actionboss.domain.post.dto.PostResponseDto;
import com.sparta.actionboss.domain.post.entity.Post;
import com.sparta.actionboss.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {
    private final S3Service s3Service;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    private static final int MAXIMUM_IMAGES = 3;    // 이미지 업로드 최대 개수

    public ResponseEntity<PostResponseDto> createPost(
            PostRequestDto postRequestDto,
            List<MultipartFile> images,
            User user
    ) throws IOException {
        if (images == null || images.isEmpty() || images.stream().allMatch(image -> image.isEmpty())) {
            throw new IllegalArgumentException("사진을 1장 이상 업로드 해주세요.");
        }
        if (images.size() > MAXIMUM_IMAGES) {
            throw new IllegalArgumentException("최대 " + MAXIMUM_IMAGES + "장의 이미지만 업로드할 수 있습니다.");
        }
        // 요청별로 폴더생성 -> 저장
        String directoryPath = "images/" + UUID.randomUUID().toString();

        List<String> imageURLs = s3Service.upload(images, directoryPath);
        Post post = new Post(postRequestDto, imageURLs, user);
        postRepository.save(post);
        return ResponseEntity.ok(new PostResponseDto(post));
    }

    public ResponseEntity<PostResponseDto> getPost(Long postId) {
        Post post = findPost(postId);
        return ResponseEntity.ok(new PostResponseDto(post));
    }

    @Transactional
    public ResponseEntity<PostResponseDto> updatePost(
            Long postId,
            PostRequestDto postRequestDto,
            User user
    ) {
        Post post = findPost(postId);
        if (hasAuthority(post, user)) {
            post.update(postRequestDto);
        } else {
            throw new IllegalArgumentException("이 게시글을 변경할 수 있는 권한이 없습니다.");
        }
        return ResponseEntity.ok(new PostResponseDto(post));
    }

    @Transactional
    public ResponseEntity<DeleteResponseDto> deletePost(
            Long postId,
            User user
    ) {
        Post post = findPost(postId);
        List<String> imageUrls = post.getImageUrls();

        if (hasAuthority(post, user)) {
            postRepository.delete(findPost(postId));
            if (!imageUrls.isEmpty()) {
                String requestFolderName = s3Service.getRequestFolderNameFromImageUrl(imageUrls.get(0));
                s3Service.deleteFolder(requestFolderName);
            }
        } else {
            throw new IllegalArgumentException("이 게시글을 변경할 수 있는 권한이 없습니다.");
        }

        return ResponseEntity.ok(new DeleteResponseDto());
    }


    private Post findPost(Long postId) {
        return postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
    }

    private boolean hasAuthority(Post post, User user) {
        return post.getUser()
                .getNickname()
                .equals(user.getNickname())
                ||
                user.getRole().equals(UserRoleEnum.ADMIN);
    }
}