package com.sparta.actionboss.domain.post.service;

import com.sparta.actionboss.domain.auth.entity.User;
import com.sparta.actionboss.domain.post.entity.Done;
import com.sparta.actionboss.domain.post.repository.DoneRepository;
import com.sparta.actionboss.domain.post.entity.Post;
import com.sparta.actionboss.domain.post.repository.PostRepository;
import com.sparta.actionboss.global.exception.PostException;
import com.sparta.actionboss.global.exception.errorcode.ClientErrorCode;
import com.sparta.actionboss.global.response.CommonResponse;
import com.sparta.actionboss.global.util.EmailUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.sparta.actionboss.global.response.SuccessMessage.*;

@Service
@RequiredArgsConstructor
public class DoneService {
    private final DoneRepository doneRepository;
    private final PostRepository postRepository;
    private final EmailUtil emailUtil;

    private static final int MAXIMUM_DONE = 5;    // 해결했어요 최대 개수

    @Transactional
    public CommonResponse createDone (Long postId, User user) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new PostException(ClientErrorCode.NO_POST));
        Optional<Done> existingDone = doneRepository.findByPostAndUser(post, user);
        if (existingDone.isEmpty()) {
            Done postDone = new Done(user, post);
            doneRepository.save(postDone);

            // 해결했어요 5개 되면 done = true
            if (post.getPostDoneList().size() >= MAXIMUM_DONE) {
                post.setDone(true);
                emailUtil.sendDoneEmail(post.getUser(), post);
            }
            return new CommonResponse<>(CREATE_DONE_MESSAGE);
        } else {
            doneRepository.delete(existingDone.get());
            return new CommonResponse<>(CANCEL_DONE_MESSAGE);
        }
    }

}