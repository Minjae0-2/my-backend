package com.minjae.my_backend.config;

import com.minjae.my_backend.domain.UserRepository;
import com.minjae.my_backend.jwt.JwtAuthenticationFilter;
import com.minjae.my_backend.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 클라이언트의 JS가 읽을 수 있게 - XSRF-TOKEN 사용 - CSRF 방어
                // 클라이언트는 해당 쿠키 값을 'X-XSRF-TOKEN' 헤더에 담아 전송
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                )

                //세션을 HttpSession -> STATELESS로 하면서 상태 비저장 = JWT
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/users/signup","/api/users/login").permitAll()
                        .anyRequest().authenticated()
                )
                //필터 추가하기
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil,userRepository), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
