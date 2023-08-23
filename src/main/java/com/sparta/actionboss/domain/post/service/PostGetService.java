package com.sparta.actionboss.domain.post.service;

import com.sparta.actionboss.domain.post.dto.MapListResponseDto;
import com.sparta.actionboss.domain.post.dto.PostListAndTotalPageResponseDto;
import com.sparta.actionboss.domain.post.dto.PostListResponseDto;
import com.sparta.actionboss.domain.post.dto.PostModalResponseDto;
import com.sparta.actionboss.domain.post.entity.Post;
import com.sparta.actionboss.domain.post.repository.AgreeRepository;
import com.sparta.actionboss.domain.post.repository.ImageRepository;
import com.sparta.actionboss.domain.post.repository.PostRepository;
import com.sparta.actionboss.global.exception.PostException;
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

import static com.sparta.actionboss.global.response.SuccessMessage.GET_POST_MESSAGE;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostGetService {

    @Value("${aws.bucket.url}")
    private String s3Url;

    private final PostRepository postRepository;
    private final AgreeRepository agreeRepository;
    private final ImageRepository imageRepository;

    public CommonResponse<PostListAndTotalPageResponseDto> getPostList(Integer page, Integer limit, String sortBy, boolean done,
                                                                       Double northLatitude, Double eastLongitude, Double southLatitude, Double westLongitude) {
        Sort.Direction direction = Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy, "createdAt");

        Pageable pageable = PageRequest.of(page, limit, sort);
        Page<Post> post;

        post = postRepository.findFilteredPosts(done, northLatitude, eastLongitude, southLatitude, westLongitude, pageable);

        List<PostListResponseDto> postListResponseDtos = post.stream()
                .map(a -> {
                    int agreeCount = agreeRepository.findByPost(a).size();
                    if (Objects.isNull(agreeCount)) {
                        agreeCount = 0;
                    }

                    String imageUrl = s3Url + "/images/" + a.getImageList().get(0).getFolderName() + "/" + a.getImageList().get(0).getImageName();

                    return new PostListResponseDto(
                            a.getPostId(),
                            a.getTitle(),
                            agreeCount,
                            a.getUser().getNickname(),
                            a.getAddress(),
                            imageUrl
                    );
                })
                .collect(Collectors.toList());

        return new CommonResponse<>(GET_POST_MESSAGE, new PostListAndTotalPageResponseDto<>(postListResponseDtos, post.getTotalPages(), page));
    }

    public CommonResponse<PostModalResponseDto> getModalPost(Long postId) {
        Post findPost = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(ClientErrorCode.NO_POST));

        int agreeCount = agreeRepository.findByPost(findPost).size();
        if (Objects.isNull(agreeCount)) {
            agreeCount = 0;
        }

        String imageUrl = s3Url + "/images/" + findPost.getImageList().get(0).getFolderName() + "/" + findPost.getImageList().get(0).getImageName();

        return new CommonResponse<>(GET_POST_MESSAGE, new PostModalResponseDto(findPost, imageUrl, agreeCount));
    }

    public CommonResponse<List<MapListResponseDto>> getMapList(boolean done,
                                                               Double northLatitude, Double eastLongitude, Double southLatitude, Double westLongitude) {
        List<Post> post;

        if (done) {
            post = postRepository.findByDoneIsTrue();
        } else {
            post = postRepository.findByDoneIsFalse();
        }
        // TODO : 로직 내 필터링 추가와 쿼리문에서 필터해서 가져오는 방법 중 고민 필요
        List<Post> filteredPosts = post.stream()
                .filter(p -> isWithinCoordinates(p, northLatitude, eastLongitude, southLatitude, westLongitude))
                .collect(Collectors.toList());

        List<MapListResponseDto> mapListResponseDto = filteredPosts.stream()
                .map(a -> new MapListResponseDto(
                        a.getPostId(),
                        a.getLatitude(),
                        a.getLongitude()

                ))
                .collect(Collectors.toList());

        return new CommonResponse<>(GET_POST_MESSAGE, mapListResponseDto);
    }

    private boolean isWithinCoordinates(Post post, Double north, Double east, Double south, Double west) {
        Double latitude = post.getLatitude();
        Double longitude = post.getLongitude();

        return latitude >= south && latitude <= north && longitude >= west && longitude <= east;
    }

}
