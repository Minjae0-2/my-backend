package com.minjae.my_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserSignUpDto {
    @NotBlank (message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank(message="비밀번호는 필수 입력 값입니다.")
    @Size(min=8, message="비밀번호는 8자 이상이어야 합니다.")
    private String password;

    @NotBlank(message="사용자 이름은 필수 입력 값입니다.")
    private String username;
}
