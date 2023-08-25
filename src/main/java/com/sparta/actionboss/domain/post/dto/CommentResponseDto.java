package com.sparta.actionboss.domain.post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.actionboss.domain.auth.entity.User;
import com.sparta.actionboss.domain.post.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentResponseDto {
    private Long id;
    private String nickname;
    private String content;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
    private LocalDateTime createdDay;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalDateTime createdTime;
    private boolean commentOwner;

    public CommentResponseDto(Comment comment, String loginUserNickname) {
        this.id = comment.getId();
        this.nickname = comment.getNickname();
        this.content = comment.getContent();
        this.createdDay = comment.getCreatedAt();
        this.createdTime = comment.getCreatedAt();
        this.commentOwner = loginUserNickname.equals(comment.getNickname());
    }
}
