package com.minjae.my_backend.dto;

import com.minjae.my_backend.domain.Post;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class PostResponseDto {
    private final Long id;
    private final String title;
    private final String content;
    private final String author;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    //entity 객체 변환
    private PostResponseDto(Post entity){
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.author = entity.getAuthor();
        this.createdAt = entity.getCreatedAt();
        this.modifiedAt = entity.getModifiedAt();
    }

    //from 메소드로만 객체 생성
    public static PostResponseDto from (Post entity){
        return new PostResponseDto(entity);
    }
}
