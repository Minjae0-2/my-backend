package com.minjae.my_backend.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final Key key;
    private final long accessTokenValidity;

    public JwtUtil(@Value("${jwt.secret}") String secretKey, @Value("1800000") long accessTokenValidity){
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.accessTokenValidity = accessTokenValidity;
    }

    public String createToken(String email){
        Date now = new Date();
        Date validity = new Date(now.getTime()+this.accessTokenValidity); //만료 시간

        return Jwts.builder()
                .setSubject(email) //토큰의 주체
                .setIssuedAt(now) //토큰이 발급된 시간
                .setExpiration(validity) // 토큰 만료된 시간
                .signWith(key, SignatureAlgorithm.HS256) //사용할 암호화 알고리즘과 비밀키
                .compact(); //JWT 문자열 만들어 반환
    }
}
