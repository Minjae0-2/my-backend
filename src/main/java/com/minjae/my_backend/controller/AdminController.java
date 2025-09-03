package com.minjae.my_backend.controller;

import com.minjae.my_backend.domain.User;
import com.minjae.my_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {
    private final UserService userService;

    @GetMapping("/users")
    public List<User> getUsers(){
        return userService.findAllUsers();
    }
}
