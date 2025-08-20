package com.minjae.my_backend.controller;

import com.minjae.my_backend.dto.PostCreateRequestDto;
import com.minjae.my_backend.dto.PostResponseDto;
import com.minjae.my_backend.dto.PostUpdateRequestDto;
import com.minjae.my_backend.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.ArrayList;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public Long createPost(@RequestBody PostCreateRequestDto requestDto){
        return postService.create(requestDto);
    }

    @GetMapping("/{id}")
    public PostResponseDto getPost(@PathVariable Long id){
        return postService.findById(id);
    }

    @PutMapping("{id}")
    public Long updatePost(@PathVariable Long id, @RequestBody PostUpdateRequestDto requestDto){
        return postService.update(id,requestDto);
    }

    @DeleteMapping("{id}")
    public Long deletePost(@PathVariable Long id){
        postService.delete(id);
        return id;
    }
}
