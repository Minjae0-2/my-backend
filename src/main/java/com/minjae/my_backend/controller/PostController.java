package com.minjae.my_backend.controller;

import com.minjae.my_backend.dto.PostCreateRequestDto;
import com.minjae.my_backend.dto.PostDeleteRequestDto;
import com.minjae.my_backend.dto.PostResponseDto;
import com.minjae.my_backend.dto.PostUpdateRequestDto;
import com.minjae.my_backend.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name="Post", description ="게시글 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @Operation(summary="게시글 생성",description = "새로운 게시글을 생성합니다.")
    @PostMapping
    public ResponseEntity<Long> createPost(@RequestBody PostCreateRequestDto requestDto){
        Long postId = postService.create(requestDto);
        //201 Created 반환
        return ResponseEntity.status(HttpStatus.CREATED).body(postId);
    }

    @Operation(summary = "게시글 하나 조회", description = "ID로 특정 게시글을 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long id){
        return ResponseEntity.ok(postService.findById(id));
    }

    @Operation(summary = "모든 게시글 조회 (페이징 처리 적용)", description = "모든 게시글을 페이징 처리를 이용해 조회합니다.")
    @GetMapping
    public ResponseEntity<Page<PostResponseDto>> getPosts(@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)Pageable pageable){ // direction: 내림차순
        return ResponseEntity.ok(postService.findAll(pageable));
    }

    @Operation(summary = "게시글 수정", description = "게시글을 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<Long> updatePost(@PathVariable Long id, @RequestBody PostUpdateRequestDto requestDto){
        return ResponseEntity.ok(postService.update(id,requestDto));
    }

    @Operation(summary = "게시글 삭제", description = "게시글을 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id){
        postService.delete(id);
        //204 No Content 반환
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "게시글 여러개 삭제", description = "게시글을 여러개 삭제합니다.")
    @DeleteMapping
    public ResponseEntity<Void> deletePosts(@RequestBody PostDeleteRequestDto requestDto){
        postService.deletePosts(requestDto.getPostIds());
        //204 No Content 반환
        return ResponseEntity.noContent().build();
    }
}
