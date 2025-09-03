package com.minjae.my_backend.domain;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter //DTO에서 사용. 데이터 꺼내기위해
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder
    public User(String email, String password, String username, Role role){
        this.email = email;
        this.password = password;
        this.username = username;
        this.role = role;
    }

    @Builder(builderClassName = "TestBuilder" ,builderMethodName = "testBuilder")
    public User(Long id, String email, String password, String username, Role role){
        this.id = id;
        this.email = email;
        this.password = password;
        this.username = username;
        this.role = role;
    }
}
