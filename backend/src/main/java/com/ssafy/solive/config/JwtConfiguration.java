package com.ssafy.solive.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtConfiguration {

    // SALT는 임의 생성 문자열
    // TODO: 아마 SALT 정보 분리 저장 및 공유가 필요해 보임
    private static final String SALT = "ssafySecret";
    private static final int ACCESS_TOKEN_EXPIRE_MINUTES = 1; // 분단위
    private static final int REFRESH_TOKEN_EXPIRE_MINUTES = 2; // 주단위

    public <T> String createAccessToken(String key, T data) {
        return create(key, data, "access-token", 1000 * 60 * ACCESS_TOKEN_EXPIRE_MINUTES);
    }

    public <T> String createRefreshToken(String key, T data) {
        return create(key, data, "refresh-token",
            1000 * 60 * 60 * 24 * 7 * REFRESH_TOKEN_EXPIRE_MINUTES);
    }

    public <T> String create(String key, T data, String subject, long expire) {
        log.info("JwtConfiguration_create_start: " + "\nkey: " + key + "\ndata: " + data.toString()
            + "\nsubject: " + subject + "\nexpire: " + expire);
        Claims claims = Jwts.claims()
            .setSubject(subject) // 토큰 제목
            .setIssuedAt(new Date()) // 생성일
            .setExpiration(new Date(System.currentTimeMillis() + expire)); // 유효기간 설정

        // 저장할 data
        claims.put(key, data);

        String jwt = Jwts.builder()
            // Header 설정 : 토큰타입, 해쉬 알고리즘
            .setHeaderParam("typ", "JWT")
            .setClaims(claims)
            // Signature 설정 : secret key를 활용한 암호화
            .signWith(SignatureAlgorithm.HS256, this.generateKey())
            .compact();
        log.info("JwtConfiguration_create_end: " + jwt);
        return jwt;
    }

    private byte[] generateKey() {
        byte[] key = null;
        try {
            key = SALT.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            if (log.isInfoEnabled()) {
                e.printStackTrace();
            } else {
                log.error("Making JWT Key Error ::: {}", e.getMessage());
            }
        }

        return key;
    }
}
