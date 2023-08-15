package com.sparta.actionboss.domain.post.service;

import com.sparta.actionboss.domain.auth.entity.User;
import com.sparta.actionboss.domain.auth.entity.UserRoleEnum;
import com.sparta.actionboss.domain.post.dto.PostRequestDto;
import com.sparta.actionboss.domain.post.dto.PostResponseDto;
import com.sparta.actionboss.domain.post.entity.Post;
import com.sparta.actionboss.domain.post.repository.PostRepository;
import com.sparta.actionboss.global.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.sparta.actionboss.global.response.SuccessMessage.CREATE_POST_MESSAGE;
import static com.sparta.actionboss.global.response.SuccessMessage.UPDATE_POST_MESSAGE;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {

    @Value("${aws.bucket.url}")
    private String s3Url;

    private final S3Service s3Service;
    private final PostRepository postRepository;

    private static final int MAXIMUM_IMAGES = 3;    // 이미지 업로드 최대 개수

    @Transactional
    public CommonResponse createPost(
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
        List<String> imageNames = images.stream().map(MultipartFile::getOriginalFilename).toList();

        Post post = new Post(postRequestDto, imageNames, user);
        postRepository.save(post);

        // 요청별로 폴더생성 -> 저장
        String directoryPath = "images/" + post.getPostId();

        List<String> imageNameList = s3Service.upload(images, directoryPath);
        post.setNames(imageNameList);
        return new CommonResponse(CREATE_POST_MESSAGE);
    }

    public ResponseEntity<PostResponseDto> getPost(Long postId) {
        Post post = findPost(postId);
        List<String> imageURLs = imageUrlPrefix(post.getImageNames(), postId);
        if (post.isDone()) {
            throw new IllegalArgumentException("이미 완료된 민원글입니다.");
            // TODO: statusCode: 421 -> DESTINATION_LOCKED ; Handler
        }
        return ResponseEntity.ok(new PostResponseDto(post, imageURLs));
    }

    @Transactional
    public CommonResponse updatePost(
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
        List<String> imageURLs = imageUrlPrefix(post.getImageNames(), postId);
        return new CommonResponse(UPDATE_POST_MESSAGE);
    }

    @Transactional
    public CommonResponse deletePost(
            Long postId,
            User user
    ) {
        Post post = findPost(postId);
        List<String> imageNames = post.getImageNames();

        if (hasAuthority(post, user)) {
            postRepository.delete(findPost(postId));
            if (!imageNames.isEmpty()) {
                s3Service.deleteFolder(postId.toString());
            }
        } else {
            throw new IllegalArgumentException("이 게시글을 삭제할 수 있는 권한이 없습니다.");
        }

        return new CommonResponse(UPDATE_POST_MESSAGE);
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

    private List<String> imageUrlPrefix(List<String> imageNames, Long postId) {
        return imageNames
                .stream()
                .map(imageName -> s3Url + "/images/" + postId + "/" + imageName)
                .toList();
    }
}