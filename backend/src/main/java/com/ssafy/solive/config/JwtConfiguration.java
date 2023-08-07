package com.ssafy.solive.config;

import com.ssafy.solive.common.exception.user.JwtTokenExpiredException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
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

    public boolean checkToken(String token) {
        try {
            Jwts.parser().setSigningKey(SALT.getBytes("UTF-8")).parseClaimsJws(token);
            return true; // token 이 유효하면 exception 발생을 안함
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) { // 우리 프로젝트에서는 이 경우만 고려
            log.info("만료된 JWT 토큰입니다.");
            throw new JwtTokenExpiredException();
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    public Long getUserId(String accessToken) {
        try {
            return Long.valueOf(
                Jwts.parser().setSigningKey(SALT.getBytes("UTF-8")).parseClaimsJws(accessToken)
                    .getBody()
                    .get("userid").toString());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
