package com.minjae.my_backend.controller;

import com.minjae.my_backend.dto.CommentCreateRequestDto;
import com.minjae.my_backend.dto.CommentResponseDto;
import com.minjae.my_backend.dto.CommentUpdateRequestDto;
import com.minjae.my_backend.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="Comment", description = "댓글 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {
    private final CommentService commentService;

    @Operation(summary = "댓글 생성", description = "특정 게시글에 새로운 댓글을 생성합니다.")
    @PostMapping
    public ResponseEntity<Long> createComment(@PathVariable Long postId, @RequestBody CommentCreateRequestDto requestDto){
        Long createdCommentID = commentService.createComment(requestDto,postId);
        return ResponseEntity.ok(createdCommentID);
    }

    @Operation(summary = "특정 게시글의 모든 댓글 조회", description = "특정 게시글에 달린 모든 댓글을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> readComments(@PathVariable Long postId){
        List<CommentResponseDto> comments = commentService.readComments(postId);
        return ResponseEntity.ok(comments);
    }

    @Operation(summary = "댓글 수정", description = "특정 댓글을 수정합니다.")
    @PutMapping("/{commentId}")
    public ResponseEntity<Long> updateComment(@PathVariable Long postId, @PathVariable Long commentId, @RequestBody CommentUpdateRequestDto requestDto){
        Long updatedCommentId = commentService.updateComment(commentId,requestDto);
        return ResponseEntity.ok(updatedCommentId);
    }

    @Operation(summary = "댓글 삭제", description = "특정 댓글을 삭제합니다.")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long postId,@PathVariable Long commentId){
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
