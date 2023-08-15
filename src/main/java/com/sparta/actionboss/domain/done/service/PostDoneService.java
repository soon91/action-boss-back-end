package com.sparta.actionboss.domain.done.service;

import com.sparta.actionboss.domain.auth.entity.User;
import com.sparta.actionboss.domain.done.dto.CancelPostDoneResponseDto;
import com.sparta.actionboss.domain.done.dto.PostDoneResponseDto;
import com.sparta.actionboss.domain.done.entity.PostDone;
import com.sparta.actionboss.domain.done.repository.PostDoneRepository;
import com.sparta.actionboss.domain.post.entity.Post;
import com.sparta.actionboss.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostDoneService {
    private final PostDoneRepository postDoneRepository;
    private final PostRepository postRepository;

    private static final int MAXIMUM_DONE = 5;    // 해결했어요 최대 개수

    @Transactional
    public ResponseEntity<?> createLike(Long postId, User user) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        Optional<PostDone> existingDone = postDoneRepository.findByPostAndUser(post, user);
        if (existingDone.isEmpty()) {
            PostDone postDone = new PostDone(user, post);
            postDoneRepository.save(postDone);
            // 해결했어요 5개 되면 done = true
            if (post.getPostDoneList().size() >= MAXIMUM_DONE) {
                post.setDone(true);
            }
            return ResponseEntity.ok(new PostDoneResponseDto());
        } else {
            postDoneRepository.delete(existingDone.get());
            return ResponseEntity.ok(new CancelPostDoneResponseDto());
        }
    }
}
