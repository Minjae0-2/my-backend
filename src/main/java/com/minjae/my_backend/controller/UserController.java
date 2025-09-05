package com.minjae.my_backend.controller;

import com.minjae.my_backend.dto.UserLoginRequestDto;
import com.minjae.my_backend.dto.UserSignUpDto;
import com.minjae.my_backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Long> signUp(@Valid @RequestBody UserSignUpDto requestDto){
        //201 Created 반환
        Long userId = userService.signUp(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userId);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginRequestDto requestDto){
        String token = userService.login(requestDto);
        return ResponseEntity.ok(token);
    }
}
