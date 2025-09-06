package com.minjae.my_backend.controller;

import com.minjae.my_backend.dto.UserLoginRequestDto;
import com.minjae.my_backend.dto.UserSignUpDto;
import com.minjae.my_backend.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
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
    public ResponseEntity<String> signUp(@Valid @RequestBody UserSignUpDto requestDto){
         userService.signUp(requestDto);
         return ResponseEntity.status(HttpStatus.CREATED).body("Sign-up Successful");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginRequestDto requestDto, HttpServletResponse response)
    {
        String token = userService.login(requestDto);
        //JWT 토큰 클라이언트로 보내기
        //Path: 프론트의 모든 경로에서 요청을 보낼때 쿠키를 보냄
        //HttpOnly: JS를 통한 쿠키 접근 막음(doucment.cookie)-> XSS방어,Http로만
        //Secure: Https 프로트콜로. SameSite=None: front와 도메인이 달라서.
        response.setHeader("Set-cookie:","authToken=" + token + "; Path=/; HttpOnly; SameSite=None; Secure; Max-Age=1800;");
        return ResponseEntity.ok("Login Successful");
    }
}
