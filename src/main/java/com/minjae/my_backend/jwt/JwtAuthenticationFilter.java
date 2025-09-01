package com.minjae.my_backend.jwt;

import com.minjae.my_backend.domain.User;
import com.minjae.my_backend.domain.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization"); //`Authorization:` 헤더를 찾고 그 다음 문자열들 저장
        String token = null;

        if(header != null && header.startsWith("Bearer ")){ //Authorization:Bearer <token>
            token= header.substring(7); //인덱스 7부터 토큰 시작-> 문자열 잘라냄
        }

        //토큰이 존재하고 유효한 경우
        if(token != null && jwtUtil.validateToken(token)){
            String email = jwtUtil.getEmailFromToken(token);
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
            //이 user 객체가 주인이다를 authentication에 저장
            Authentication authentication = new UsernamePasswordAuthenticationToken(user,null,null);
            //발급한 티켓을 기록 - ThreadLocal(getContext가 thread마다 제공)로 각 thread에 SecurityContext(Authentication)를 담음
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        //다음 필터로
        filterChain.doFilter(request,response);
    }
}
