package com.minjae.my_backend.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    //post 객체로 모든 댓글 찾기
    List<Comment> findAllByPost(Post post);
}
