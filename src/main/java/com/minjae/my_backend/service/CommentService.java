package com.minjae.my_backend.service;

import com.minjae.my_backend.domain.*;
import com.minjae.my_backend.dto.CommentCreateRequestDto;
import com.minjae.my_backend.dto.CommentResponseDto;
import com.minjae.my_backend.dto.CommentUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    //게시글의 댓글 생성하기
    @Transactional
    public Long createComment(CommentCreateRequestDto requestDto, Long postId){
        User currentUser = getCurrentUser();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 작성할 게시글이 없습니다."));

        Comment newComment = Comment.builder()
                .content(requestDto.getContent())
                .user(currentUser)
                .post(post)
                .build();

        Comment savedComment = commentRepository.save(newComment);
        return savedComment.getId();
    }

    //게시글의 댓글 전체 읽기
    @Transactional(readOnly = true)
    public List<CommentResponseDto> readComments(Long postId){
        //해당 게시글이 존재하는지 확인
        Post post = postRepository.findById(postId)
                .orElseThrow(()->new IllegalArgumentException("댓글을 조회할 게시글이 없습니다. id="+ postId));
        //댓글 목록 조회
        List<Comment> comments = commentRepository.findAllByPost(post);

        //조회한 entity리스트를 DTO 리스트로 변환
        return comments.stream()
                .map(CommentResponseDto :: from)
                .collect(Collectors.toList());
    }

    //댓글 수정
    @Transactional
    public Long updateComment(Long commentId, CommentUpdateRequestDto requestDto){
        User currentUser = getCurrentUser();
        //해당 댓글 조회
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()->new IllegalArgumentException("해당 댓글이 없습니다. id="+commentId));
        //권한 확인
        if(!comment.getUser().getId().equals(currentUser.getId())){
            throw new IllegalStateException("댓글을 수정할 권한이 없습니다.");
        }
        comment.update(requestDto.getContent());
        return commentId;
    }

    //댓글 삭제
    @Transactional
    public void deleteComment(Long commentId){
        User currentUser = getCurrentUser();
        //삭제할 댓글 조회
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()->new IllegalArgumentException("해당 댓글이 없습니다. id="+ commentId));
        //권한 확인
        if(!comment.getUser().getId().equals(currentUser.getId())){
            throw new IllegalStateException("댓글을 삭제할 권한이 없습니다.");
        }
        commentRepository.delete(comment);
    }


    private User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication==null || authentication.getName()==null){
            throw new IllegalStateException("인증된 사용자 정보를 찾을 수 없습니다.");
        }
        if(authentication.getPrincipal() instanceof  User){
            return (User)authentication.getPrincipal();
        }else{
            String userEmail = authentication.getName();
            return userRepository.findByEmail(userEmail)
                    .orElseThrow(()-> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));
        }
    }
}
