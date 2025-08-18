package com.minjae.my_backend.dto;

import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class PostResponseDto {
    private final long id;
    private final String title;
    private final String content;
    private final String author;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    //repository 객체 변환 필요
    private PostResponseDto(Post entity){
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.author = entity.getAuthor();
        this.createdAt = entity.getCreatedAt();
        this.modifiedAt = entity.getModifiedAt();
    }

    //repository 객체 변환 - from 메소드로
    public static PostResponseDto from (Post entity){
        return new PostResponseDto(entity);
    }
}
