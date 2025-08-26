package com.minjae.my_backend.service;

import com.minjae.my_backend.domain.User;
import com.minjae.my_backend.domain.UserRepository;
import com.minjae.my_backend.dto.UserLoginRequestDto;
import com.minjae.my_backend.dto.UserSignUpDto;
import com.minjae.my_backend.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public Long signUp(UserSignUpDto requestDto){
        if(userRepository.findByEmail(requestDto.getEmail()).isPresent()){
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        User newUser = User.builder()
                .email(requestDto.getEmail())
                .password(encodedPassword)
                .username(requestDto.getUsername())
                .build();

        User savedUser = userRepository.save(newUser);
        return savedUser.getId();
    }

    @Transactional(readOnly = true)
    public String login(UserLoginRequestDto requestDto){
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));

        if(!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");

        }

        return jwtUtil.createToken(user.getEmail());
    }
}
