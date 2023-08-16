package com.sparta.actionboss.domain.post.service;

import com.sparta.actionboss.domain.auth.entity.User;
import com.sparta.actionboss.domain.post.entity.Agree;
import com.sparta.actionboss.domain.post.entity.Post;
import com.sparta.actionboss.domain.post.repository.AgreeRepository;
import com.sparta.actionboss.domain.post.repository.PostRepository;
import com.sparta.actionboss.global.exception.AgreeException;
import com.sparta.actionboss.global.exception.errorcode.ClientErrorCode;
import com.sparta.actionboss.global.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.sparta.actionboss.global.response.SuccessMessage.*;


@Service
@RequiredArgsConstructor
public class AgreeService {

    private final PostRepository postRepository;
    private final AgreeRepository agreeRepository;

    public CommonResponse agreePost(Long postId, User user) {
        Post post = postRepository.findById(postId).orElseThrow(
                ()-> new AgreeException(ClientErrorCode.NO_POST));

        if(!agreeRepository.existsAgreeByUserAndPost(user, post)){
            Agree agree = new Agree(user, post);
            agreeRepository.save(agree);
            return new CommonResponse(CREATE_AGREE,null);
        } else {
            Agree agree = agreeRepository.findByUserAndPost(user, post).orElseThrow(
                    ()-> new AgreeException(ClientErrorCode.NO_AGREE));
            agreeRepository.delete(agree);
            return new CommonResponse(CANCEL_AGREE,null);
        }
    }
}
