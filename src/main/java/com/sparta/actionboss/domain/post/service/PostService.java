package com.sparta.actionboss.domain.post.service;

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

    private static final int MAXIMUM_IMAGES = 3;    // 이미지 업로드 최대 개수

    public ResponseEntity<PostResponseDto> createPost(PostRequestDto postRequestDto, List<MultipartFile> images) throws IOException {
        if (images == null || images.isEmpty() || images.stream().allMatch(image -> image.isEmpty())) {
            throw new IllegalArgumentException("사진을 1장 이상 업로드 해주세요.");
        }

        if (images.size() > MAXIMUM_IMAGES) {
            throw new IllegalArgumentException("최대 " + MAXIMUM_IMAGES + "장의 이미지만 업로드할 수 있습니다.");
        }
        // 요청별로 폴더생성 -> 저장
        String directoryPath = "images/" + UUID.randomUUID().toString();

        List<String> imageURLs = s3Service.upload(images, directoryPath);
        Post post = new Post(postRequestDto, imageURLs);
        postRepository.save(post);

        return ResponseEntity.ok(new PostResponseDto(post));
    }

    public ResponseEntity<PostResponseDto> getPost(Long postId) {
        Post post = findPost(postId);
        return ResponseEntity.ok(new PostResponseDto(post));
    }

    @Transactional
    public ResponseEntity<PostResponseDto> updatePost(Long postId, PostRequestDto postRequestDto) {
        Post post = findPost(postId);
        post.update(postRequestDto);
        return ResponseEntity.ok(new PostResponseDto(post));
    }

//    @Transactional
//    public ResponseEntity<DeleteResponseDto> deletePost(Long postId) {
//        Post post = findPost(postId);
//        postRepository.delete(post);
//        String folderName = post.getImageUrls().get(0).substring(S3_BUCKET_URL - 1, S3_BUCKET_URL + UUID_RANDOM_UUID_SIZE);
//        System.out.println("folderName = " + folderName);
//        s3Service.removeFolder(folderName);
//        return ResponseEntity.ok(new DeleteResponseDto());
//    }

    @Transactional
    public ResponseEntity<DeleteResponseDto> deletePost(Long postId) {
        List<String> imageUrls = findPost(postId).getImageUrls();
        postRepository.delete(findPost(postId));
        if (!imageUrls.isEmpty()) {
            String requestFolderName = s3Service.getRequestFolderNameFromImageUrl(imageUrls.get(0));
            System.out.println("folderName = " + requestFolderName);
            s3Service.deleteFolder(requestFolderName);
        }
        return ResponseEntity.ok(new DeleteResponseDto());
    }


    private Post findPost(Long postId) {
        return postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
    }


}

