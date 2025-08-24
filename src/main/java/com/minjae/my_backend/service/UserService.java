package com.minjae.my_backend.service;

import com.minjae.my_backend.domain.User;
import com.minjae.my_backend.domain.UserRepository;
import com.minjae.my_backend.dto.UserSignUpDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
}
