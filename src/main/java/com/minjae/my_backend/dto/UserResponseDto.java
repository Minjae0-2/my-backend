package com.minjae.my_backend.dto;

import com.minjae.my_backend.domain.Role;
import com.minjae.my_backend.domain.User;
import lombok.Getter;

@Getter
public class UserResponseDto {
    private final Long id;
    private final String email;
    private final String username;
    private final Role role;

    private UserResponseDto(User entity){
        this.id = entity.getId();
        this.email = entity.getEmail();
        this.username = entity.getUsername();
        this.role = entity.getRole();
    }

    public static UserResponseDto from(User entity){
        return new UserResponseDto(entity);
    }
}
