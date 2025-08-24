package com.minjae.my_backend.controller;

import com.minjae.my_backend.dto.UserSignUpDto;
import com.minjae.my_backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public Long signUp(@Valid @RequestBody UserSignUpDto requestDto){
        return userService.signUp(requestDto);
    }
}
