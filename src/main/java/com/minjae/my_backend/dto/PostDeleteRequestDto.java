package com.minjae.my_backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter //프론트가 보낸 list DTO 객체로 변환
public class PostDeleteRequestDto {
    private List<Long> postIds;
}
