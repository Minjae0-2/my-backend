package com.minjae.my_backend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.ArrayList;

@RestController
public class PostController {
    @GetMapping("/posts")
    public List<String> getPosts(){
        return new ArrayList<>();
    }
}
