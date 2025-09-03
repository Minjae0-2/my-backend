package com.minjae.my_backend.service;

import com.minjae.my_backend.domain.Post;
import com.minjae.my_backend.domain.PostRepository;
import com.minjae.my_backend.domain.User;
import com.minjae.my_backend.domain.UserRepository;
import com.minjae.my_backend.dto.PostCreateRequestDto;
import com.minjae.my_backend.dto.PostResponseDto;
import com.minjae.my_backend.dto.UserSignUpDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) //Mockito 프레임워크 사용
class PostServiceTest {

    @InjectMocks //Mock 객체들을 주입받음 - 테스트 대상 클래스
    private PostService postService;

    @Mock //가짜 PostRepository 객체
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    //given절에서 사용할 User객체
    private User mockUser;

    @BeforeEach // @Test 시작 전 실행되는 설정 메소드
    void setUp(){
        mockUser = User.testBuilder()
                .id(1L)
                .email("test@example.com")
                .username("test")
                .build();
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        //postService의 getCurrentUser
        when(securityContext.getAuthentication()).thenReturn(auth);
        when(auth.getName()).thenReturn("test@example.com");
        when(auth.getPrincipal()).thenReturn(mockUser);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));

        SecurityContextHolder.setContext(securityContext);
    }

    @Test //테스트 메소드
    @DisplayName("게시글 단건 조회 - 성공")
    void findById_success(){
        //given
        Long postId = 1L;
        Post post = Post.testBuilder()
                .id(postId)
                .title("테스트 제목")
                .content("테스트 내용")
                .user(mockUser)
                .build();
        // findById 호출 시 post객체를 Optional로 감싸서 보냄
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        //when - postService 실제 코드를 실행함
        PostResponseDto responseDto = postService.findById(postId);

        //then
        assertThat(responseDto.getId()).isEqualTo(postId);
        assertThat(responseDto.getTitle()).isEqualTo("테스트 제목");
        assertThat(responseDto.getContent()).isEqualTo("테스트 내용");
    }

    @Test
    @DisplayName("게시글 단건 조회 - 실패(존재하지 않는 ID)")
    void findById_fail(){
        //given
        Long nonExistentPostId = 999L;

        when(postRepository.findById(nonExistentPostId)).thenReturn(Optional.empty());

        //when & then
        assertThrows(IllegalArgumentException.class, ()->{
            postService.findById(nonExistentPostId);
        });
    }

    @Test
    @DisplayName("게시글 생성 - 성공")
    void create_success(){
        //given
        PostCreateRequestDto requestDto = new PostCreateRequestDto();
        requestDto.setTitle("테스트 제목");
        requestDto.setContent("테스트 내용");

        //Service 내부에서 builder로 만들어질 post객체의 저장 후 모습
        Post savedPost = Post.testBuilder()
                .id(1L)
                .title("테스트 제목")
                .content("테스트 내용")
                .user(mockUser)
                .build();
        //postService에서 builder을 이용해서 post가 만들어지므로 any를 이용해 모든 Post객체를 통과
        when(postRepository.save(any(Post.class))).thenReturn(savedPost);

        Long postId = postService.create(requestDto);

        //then
        assertThat(postId).isEqualTo(1L);
        //시도 횟수
        verify(postRepository,times(1)).save(any(Post.class));
    }
}