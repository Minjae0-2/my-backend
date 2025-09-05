package com.minjae.my_backend.dto;

import com.minjae.my_backend.domain.Comment;
import com.minjae.my_backend.domain.Post;
import com.minjae.my_backend.domain.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {
    private final Long id;
    private final String content;
    private final String author;
    private final LocalDateTime createdAt;

    private CommentResponseDto(Comment entity){
        this.id = entity.getId();
        this.content = entity.getContent();
        this.author = entity.getUser().getUsername();
        this.createdAt = entity.getCreatedAt();
    }

    public static CommentResponseDto from (Comment entity){
        return new CommentResponseDto(entity);
    }
}
