package com.minjae.my_backend.controller;

import com.minjae.my_backend.dto.PostCreateRequestDto;
import com.minjae.my_backend.dto.PostResponseDto;
import com.minjae.my_backend.dto.PostUpdateRequestDto;
import com.minjae.my_backend.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="Post", description ="게시글 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @Operation(summary="게시글 생성",description = "새로운 게시글을 생성합니다.")
    @PostMapping
    public Long createPost(@RequestBody PostCreateRequestDto requestDto){
        return postService.create(requestDto);
    }

    @Operation(summary = "게시글 하나 조회", description = "ID로 특정 게시글을 조회합니다.")
    @GetMapping("/{id}")
    public PostResponseDto getPost(@PathVariable Long id){
        return postService.findById(id);
    }

    @Operation(summary = "모든 게시글 조회", description = "모든 게시글을 조회합니다.")
    @GetMapping
    public List<PostResponseDto> getPosts(){
        return postService.findAll();
    }

    @Operation(summary = "게시글 수정", description = "게시글을 수정합니다.")
    @PutMapping("{id}")
    public Long updatePost(@PathVariable Long id, @RequestBody PostUpdateRequestDto requestDto){
        return postService.update(id,requestDto);
    }

    @Operation(summary = "게시글 삭제", description = "게시글을 삭제합니다.")
    @DeleteMapping("{id}")
    public Long deletePost(@PathVariable Long id){
        postService.delete(id);
        return id;
    }
}
