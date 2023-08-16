package com.sparta.actionboss.domain.post.service;

import com.sparta.actionboss.domain.auth.entity.User;
import com.sparta.actionboss.domain.auth.entity.UserRoleEnum;
import com.sparta.actionboss.domain.post.repository.PostDoneRepository;
import com.sparta.actionboss.domain.post.dto.PostRequestDto;
import com.sparta.actionboss.domain.post.dto.PostResponseDto;
import com.sparta.actionboss.domain.post.entity.Post;
import com.sparta.actionboss.domain.post.repository.AgreeRepository;
import com.sparta.actionboss.domain.post.repository.PostRepository;
import com.sparta.actionboss.global.exception.PostException;
import com.sparta.actionboss.global.exception.errorcode.ClientErrorCode;
import com.sparta.actionboss.global.response.CommonResponse;
import com.sparta.actionboss.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.sparta.actionboss.global.response.SuccessMessage.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {

    @Value("${aws.bucket.url}")
    private String s3Url;

    private final S3Service s3Service;
    private final PostRepository postRepository;
    private final PostDoneRepository postDoneRepository;
    private final AgreeRepository agreeRepository;

    private static final int MAXIMUM_IMAGES = 3;    // 이미지 업로드 최대 개수

    @Transactional
    public CommonResponse createPost(
            PostRequestDto postRequestDto,
            List<MultipartFile> images,
            User user
    ) throws IOException {

        if (limitImage(images)) {
            throw new PostException(ClientErrorCode.UPLOAD_NO_IMAGE);
        }
        if (images.size() > MAXIMUM_IMAGES) {
            throw new PostException(ClientErrorCode.UPLOAD_MAXIMUM_IMAGE);
        }
        List<String> imageNames = images.stream().map(MultipartFile::getOriginalFilename).toList();

        Post post = new Post(postRequestDto, imageNames, user);
        postRepository.save(post);

        // 요청별로 폴더생성 -> 저장
        String directoryPath = "images/" + post.getPostId();

        List<String> imageNameList = s3Service.upload(images, directoryPath);
        post.setNames(imageNameList);
        return new CommonResponse(CREATE_POST_MESSAGE, new PostResponseDto(post.getPostId()));
    }

    public CommonResponse<PostResponseDto> getPost(Long postId) {
        Optional<UserDetailsImpl> userDetails = Optional.ofNullable(getUserDetails());
        Post post = findPost(postId);
        List<String> imageURLs = imageUrlPrefix(post.getImageNames(), postId);
        boolean done = false;
        boolean owner = false;
        boolean agree = false;

        if (userDetails.isPresent()) {
            User loginUser = userDetails.get().getUser();
            done = postDoneRepository.findByPostAndUser(post, loginUser).isPresent();
            agree = agreeRepository.findByUserAndPost(loginUser, post).isPresent();

            owner = post.getUser().getNickname().equals(loginUser.getNickname());
        }
        if (post.isDone()) {
            throw new PostException(ClientErrorCode.ALREADY_DONE_POST);
        }

        return new CommonResponse<>(GET_POST_MESSAGE, new PostResponseDto(post, imageURLs, done, owner, agree));
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
            throw new PostException(ClientErrorCode.NO_PERMISSION_UPDATE);
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
            throw new PostException(ClientErrorCode.NO_PERMISSION_DELETE);
        }

        return new CommonResponse(DELETE_POST_MESSAGE);
    }

    // 해당 게시글 찾기
    private Post findPost(Long postId) {
        return postRepository.findById(postId).orElseThrow(
                () -> new PostException(ClientErrorCode.NO_POST));
    }

    // 권한 확인
    private boolean hasAuthority(Post post, User user) {
        return post.getUser()
                .getNickname()
                .equals(user.getNickname())
                ||
                user.getRole().equals(UserRoleEnum.ADMIN);
    }

    private boolean limitImage(List<MultipartFile> images) {
        return images == null
                ||
                images.isEmpty()
                ||
                images.stream().allMatch(image -> image.isEmpty());
    }


    // 이미지 파일명에 URL Prefix 붙이기
    private List<String> imageUrlPrefix(List<String> imageNames, Long postId) {
        return imageNames
                .stream()
                .map(imageName -> s3Url + "/images/" + postId + "/" + imageName)
                .toList();
    }

    // 로그인을 하지 않을 경우, 로그인을 한 경우 -> UserDetailsImpl 이 들어오는 경우, 들어오지 않는 경우
    private UserDetailsImpl getUserDetails() {
        UserDetailsImpl userDetails = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof UserDetailsImpl) {
            userDetails = (UserDetailsImpl) authentication.getPrincipal();
        }
        return userDetails;
    }
}