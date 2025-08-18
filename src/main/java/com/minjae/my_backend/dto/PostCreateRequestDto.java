package com.minjae.my_backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter // Service에서 DTO내용 읽으려고 사용
@Setter // Jacksond이 JSON 객체 자바 객체로
@NoArgsConstructor
public class PostCreateRequestDto {
    private String title;
    private String content;
}
