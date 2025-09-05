package com.minjae.my_backend.service;

import com.minjae.my_backend.domain.Post;
import com.minjae.my_backend.domain.PostRepository;
import com.minjae.my_backend.domain.User;
import com.minjae.my_backend.domain.UserRepository;
import com.minjae.my_backend.dto.PostCreateRequestDto;
import com.minjae.my_backend.dto.PostResponseDto;
import com.minjae.my_backend.dto.PostUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor //final 필드에 대한 생성자 생성
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    //게시물 생성
    @Transactional
    public Long create(PostCreateRequestDto requestDto){
        //현재 로그인한 사용자 가져오기
        User currentUser = getCurrentUser();

        Post newPost = Post.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .user(currentUser)
                .build();

        // 완성된 entity DB에 저장 후 Id 반환
        Post savedPost = postRepository.save(newPost);
        return savedPost.getId();
    }

    //게시물 여러개 조회
    @Transactional(readOnly = true)
    public Page<PostResponseDto> findAll(Pageable pageable){
        Page<Post> postPage = postRepository.findAll(pageable);
        return postPage.map(PostResponseDto::from);
    }

    //게시물 1개 조회
    @Transactional(readOnly = true)
    public PostResponseDto findById(Long id){
        Post postEntity = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
        return PostResponseDto.from(postEntity);
    }

    //게시물 업데이트
    @Transactional
    public Long update(Long id, PostUpdateRequestDto requestDto){

        User currentUser = getCurrentUser();
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        // 게시물 작성자와 현재 로그인한 사용자 같은지 ID로 비교
        if(!post.getUser().getId().equals(currentUser.getId())){
            throw new IllegalStateException("게시물을 수정할 권한이 없습니다.");
        }

        post.update(requestDto.getTitle(), requestDto.getContent());
        return id;
    }

    //게시물 삭제
    @Transactional
    public void delete(Long id){
        User currentUser = getCurrentUser();
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        if(!post.getUser().getId().equals(currentUser.getId())) {
            throw new IllegalStateException("해당 게시글을 삭제할 권한이 없습니다.");
        }
        postRepository.delete(post);

    }

    //게시물 여러개 삭제
    @Transactional
    public void deletePosts(List<Long> ids){
        User currentUser = getCurrentUser();

        //조회를 우선적으로 함(권한이 있는지 없는지를 위해)
        List<Post> postsToDelete= postRepository.findAllById(ids);

        //조회된 각 게시글 권한 확인
        for(Post post: postsToDelete){
            //하나라도 틀리면 에러
            if (!post.getUser().getId().equals(currentUser.getId())) {
                throw new IllegalStateException("선택한 게시글 중 삭제할 권한이 없는 게시글이 포함되어 있습니다.");
            }
        }
        //일괄 삭제
        postRepository.deleteAllInBatch(postsToDelete);
    }

    //JWT 필터에서 SecurityContext에 저장한 User 객체 꺼내오는 메소드
    private User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new IllegalStateException("인증된 사용자 정보를 찾을 수 없습니다.");
        }
        //getPrincipal은 Object객체를 리턴하므로 User인지 instanceof로 타입 확인
        if(authentication.getPrincipal() instanceof User){
            return (User) authentication.getPrincipal();
        } else{
            // Principal이 UserDetails 타입인 경우를 대비
            String userEmail = authentication.getName();
            return userRepository.findByEmail(userEmail)
                    .orElseThrow(()->new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));
        }
        }
    }

