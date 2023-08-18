package com.ssafy.solive.config;

import com.ssafy.solive.common.exception.user.JwtTokenExpiredException;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@Slf4j
@Configuration
public class JwtConfiguration {

    @Value("${security.jwt.salt}")
    private String SALT;
    private static final int ACCESS_TOKEN_EXPIRE_MINUTES = 60 * 9; // 분단위
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

        // PayLoad에 저장할 Claims 객체
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
        log.info("JwtConfiguration_generate_start");
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
        log.info("JwtConfiguration_generate_end: " + key);
        return key;
    }

    public boolean checkToken(String token) {
        log.info("JwtConfiguration_checkToken_start: " + token);
        if (token == null) {
            log.info("access-token이 존재하지 않습니다.");
            log.info("JwtConfiguration_checkToken_end: false");
            return false;
        }

        try {
            Jwts.parser().setSigningKey(SALT.getBytes("UTF-8")).parseClaimsJws(token);
            log.info("JwtConfiguration_checkToken_end: True");
            return true; // token 이 유효하면 exception 발생을 안함
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) { // 우리 프로젝트에서는 이 경우만 고려
            log.info("만료된 JWT 토큰입니다.");
            log.info("JwtConfiguration_checkToken_end: ExpiredJwtException");
            throw new JwtTokenExpiredException();
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        log.info("JwtConfiguration_checkToken_end: false");
        return false;
    }

    public Long getUserId(String accessToken) {
        log.info("JwtConfiguration_getUserId_start: " + accessToken);
        try {
            Long userId = Long.valueOf(
                    Jwts.parser().setSigningKey(SALT.getBytes("UTF-8")).parseClaimsJws(accessToken)
                            .getBody()
                            .get("userId").toString());
            log.info("JwtConfiguration_getUserId_end: " + userId);
            return userId;
        } catch (UnsupportedEncodingException e) {
            log.info("JwtConfiguration_getUserId_end: UnsupportedEncodingException");
            throw new RuntimeException(e);
        }
    }
}
