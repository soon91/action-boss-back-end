package com.sparta.actionboss.domain.post.service;

import com.sparta.actionboss.domain.auth.entity.User;
import com.sparta.actionboss.domain.auth.entity.UserRoleEnum;
import com.sparta.actionboss.domain.post.dto.CommentRequestDto;
import com.sparta.actionboss.domain.post.entity.Comment;
import com.sparta.actionboss.domain.post.entity.Post;
import com.sparta.actionboss.domain.post.repository.CommentRepository;
import com.sparta.actionboss.domain.post.repository.PostRepository;
import com.sparta.actionboss.global.exception.CommentException;
import com.sparta.actionboss.global.exception.PostException;
import com.sparta.actionboss.global.exception.errorcode.ClientErrorCode;
import com.sparta.actionboss.global.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.sparta.actionboss.global.response.SuccessMessage.CREATE_COMMENT_MESSAGE;
import static com.sparta.actionboss.global.response.SuccessMessage.DELETE_COMMENT_MESSAGE;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;


    // 댓글 작성
    public CommonResponse createComment(Long postId, CommentRequestDto commentRequestDto, User user) {
        Post post = findPost(postId);
        Comment comment = new Comment(post, user, commentRequestDto.getContent());
        commentRepository.save(comment);

        return new CommonResponse(CREATE_COMMENT_MESSAGE);

    }

    // 댓글 삭제
    public CommonResponse deleteComment(Long commentId, User user) {
        Comment comment = findComment(commentId);
        if (hasAuthority(comment, user)) {
            commentRepository.delete(comment);
        } else {
            throw new CommentException(ClientErrorCode.NO_PERMISSION_COMMENT_DELETE);
        }
        return new CommonResponse(DELETE_COMMENT_MESSAGE);
    }

    // 댓글 조회 -> PostService getPost에서 호출
    public List<Comment> findComments(Long postId) {
        return commentRepository.findCommentsByPostId(postId);
    }

    // Post 있는지 확인
    private Post findPost(Long postId) {
        return postRepository.findById(postId).orElseThrow(
                () -> new PostException(ClientErrorCode.NO_POST));
    }


    // Comment 있는지 확인
    private Comment findComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new CommentException(ClientErrorCode.NO_COMMENT));
    }

    // 댓글 삭제 권한 확인
    private boolean hasAuthority(Comment comment, User user) {
        return comment.getUser()
                .getNickname()
                .equals(user.getNickname())
                ||
                user.getRole().equals(UserRoleEnum.ADMIN);
    }
}
