package com.sparta.actionboss.domain.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.actionboss.domain.auth.entity.User;
import com.sparta.actionboss.domain.auth.entity.UserRoleEnum;
import com.sparta.actionboss.domain.comment.entity.Comment;
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

    public CommentResponseDto(Comment comment, User loginUser) {
        this.id = comment.getId();
        this.nickname = comment.getUser().getNickname();
        this.content = comment.getContent();
        this.createdDay = comment.getCreatedAt();
        this.createdTime = comment.getCreatedAt();
        if(loginUser != null) {
            this.commentOwner = loginUser.getUserId().equals(comment.getUser().getUserId())
                    || loginUser.getRole().equals(UserRoleEnum.ADMIN);
        } else {
            this.commentOwner = false;
        }
    }
}
