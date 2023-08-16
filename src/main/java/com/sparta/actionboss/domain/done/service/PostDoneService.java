package com.sparta.actionboss.domain.done.service;

import com.sparta.actionboss.domain.auth.entity.User;
import com.sparta.actionboss.domain.done.entity.PostDone;
import com.sparta.actionboss.domain.done.repository.PostDoneRepository;
import com.sparta.actionboss.domain.post.entity.Post;
import com.sparta.actionboss.domain.post.repository.PostRepository;
import com.sparta.actionboss.global.exception.PostException;
import com.sparta.actionboss.global.exception.errorcode.ClientErrorCode;
import com.sparta.actionboss.global.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.sparta.actionboss.global.response.SuccessMessage.*;

@Service
@RequiredArgsConstructor
public class PostDoneService {
    private final PostDoneRepository postDoneRepository;
    private final PostRepository postRepository;

    private static final int MAXIMUM_DONE = 5;    // 해결했어요 최대 개수

    @Transactional
    public void createDone (Long postId, User user) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new PostException(ClientErrorCode.NO_POST));
        Optional<PostDone> existingDone = postDoneRepository.findByPostAndUser(post, user);
        if (existingDone.isEmpty()) {
            PostDone postDone = new PostDone(user, post);
            postDoneRepository.save(postDone);

            // 해결했어요 5개 되면 done = true
            if (post.getPostDoneList().size() >= MAXIMUM_DONE) {
                post.setDone(true);
            }
        } else {
            postDoneRepository.delete(existingDone.get());
        }
    }
}
