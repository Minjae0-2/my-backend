package com.minjae.my_backend.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access= AccessLevel.PROTECTED) //Protected인 이유는 JPA가 이 객체를 상속하는 proxy 객체를 생성하기 때문 (lazy loading)
public class Post {
    @Id //primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //제목은 100바이트만 사용 (한글은 1개당 3바이트)
    @Column(length = 100, nullable = false)
    private String title;

    //65535 바이트 = TEXT
    @Column(columnDefinition = "TEXT", nullable=false )
    private String content;

    private String author;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    //Service에서 프론트에서 데이터 받아오면 DB에 저장하기 위해
    //객체 생성할때 무조건 Builder 이용해서 생성해야함
    @Builder
    public Post(String title, String content, String author){
        this.title = title;
        this.content = content;
        this.author = author;
    }

}
