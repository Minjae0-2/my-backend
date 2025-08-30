package com.minjae.my_backend.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j(topic =  "JwtUtil") //로그 추가
@Component
public class JwtUtil {

    private final Key key;
    private final long accessTokenValidity;

    public JwtUtil(@Value("${jwt.secret}") String secretKey, @Value("1800000") long accessTokenValidity){
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.accessTokenValidity = accessTokenValidity;
    }

    //토큰 생성
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

    // 토큰에서 이메일 추출
    public String getEmailFromToken(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key) // 토큰 검증하기 위해 key 설정
                .build() // 파서 객체 생성
                .parseClaimsJws(token)// 서명 검증, 토큰 파싱하고 Claims 객체 얻음 (토큰에 담긴 데이터 덩어리)
                .getBody() // Claims 객체 가져옴
                .getSubject(); //subject 가져옴 - 이메일
    }

    //토큰 유효성 검증
    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        }catch(SignatureException | MalformedJwtException e){ //토큰의 서명이 유효하지 않을 때, 토큰의 형식이 잘못되었을 때
            log.error("Invalid JWT signature, 유효하지 않은 JWT 서명입니다.");
        }catch(ExpiredJwtException e){
            log.error("Expired JWT token, 만료된 JWT 토큰 입니다.");
        }catch(UnsupportedJwtException e){ //ex.JWE
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        }catch(IllegalArgumentException e){ //토큰 문자열이 비어있거나 null같이 잘못된 인자 전달
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }
}
