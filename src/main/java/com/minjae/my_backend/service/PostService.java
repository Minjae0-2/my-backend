package com.minjae.my_backend.service;

import com.minjae.my_backend.domain.Post;
import com.minjae.my_backend.domain.PostRepository;
import com.minjae.my_backend.dto.PostCreateRequestDto;
import com.minjae.my_backend.dto.PostResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor //final 필드에 대한 생성자 생성
public class PostService {
    private final PostRepository postRepository;

    //게시물 생성
    @Transactional
    public Long create(PostCreateRequestDto requestDto){
        //DB에 저장하기 위해
        String author = "minjae"; // Todo: 사용자 정보 가져오기

        Post newPost = Post.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .author(author)
                .build();

        // 완성된 entity DB에 저장 후 Id 반환
        Post savedPost = postRepository.save(newPost);
        return savedPost.getId();
    }

    //게시물 조회
    @Transactional(readOnly = true)
    public PostResponseDto findById(Long id){
        Post postEntity = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        return PostResponseDto.from(postEntity);
    }
}
