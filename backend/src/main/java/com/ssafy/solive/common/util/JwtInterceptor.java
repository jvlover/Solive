package com.ssafy.solive.common.util;

import com.ssafy.solive.api.user.service.UserService;
import com.ssafy.solive.common.exception.user.JwtInvalidException;
import com.ssafy.solive.common.exception.user.JwtTokenExpiredException;
import com.ssafy.solive.config.JwtConfiguration;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtInterceptor {

    private final UserService userService;
    private final JwtConfiguration jwtConfiguration;

    @Autowired
    public JwtInterceptor(UserService userService, JwtConfiguration jwtConfiguration) {
        this.userService = userService;
        this.jwtConfiguration = jwtConfiguration;
    }

    /**
     * accessToken이 유효하면 true, 만료됐으면 JwtTokenExpiredException, 이외는 false
     *
     * @param request current HTTP request
     */
    public Long checkValidAndGetUserId(HttpServletRequest request) {
        log.info("JwtInterceptor_preHandle_start");
        try {
            String accessToken = request.getHeader("access-token");
            // 로그아웃 상태가 아닌지 확인
            boolean isLogout = userService.isLogout(accessToken);
            log.info("JwtInterceptor_checkValidAndGetUserId_mid: " + isLogout);

            // accessToken이 유효한지 확인
            boolean checkToken = jwtConfiguration.checkToken(accessToken);
            log.info("JwtInterceptor_checkValidAndGetUserId_end: true");

            if (!isLogout && checkToken) {
                Long userId = userService.getUserIdByToken(accessToken);
                log.info("JwtInterceptor_checkValidAndGetUserId_end: " + userId);
                return userId;
            } else {
                log.info("JwtInterceptor_checkValidAndGetUserId_end: Invalid User");
                return -1L;
            }
        } catch (ExpiredJwtException e) {
            log.info("JwtInterceptor_checkValidAndGetUserId_end: JwtTokenExpiredException");
            throw new JwtTokenExpiredException();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("JwtInterceptor_checkValidAndGetUserId_end: JwtInvalidException");
            throw new JwtInvalidException();
        }
    }
}
