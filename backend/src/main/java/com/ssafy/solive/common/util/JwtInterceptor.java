package com.ssafy.solive.common.util;

import com.ssafy.solive.api.user.service.UserService;
import com.ssafy.solive.common.exception.user.JwtInvalidException;
import com.ssafy.solive.common.exception.user.JwtTokenExpiredException;
import com.ssafy.solive.config.JwtConfiguration;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class JwtInterceptor implements HandlerInterceptor {

    private final UserService userService;
    private final JwtConfiguration jwtConfiguration;

    @Autowired
    public JwtInterceptor(UserService userService, JwtConfiguration jwtConfiguration) {
        this.userService = userService;
        this.jwtConfiguration = jwtConfiguration;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("JwtInterceptor_preHandle_start");
        try {
            String accessToken = request.getHeader("access-token");
            // 로그아웃 상태가 아닌지 확인
            boolean isLogout = userService.isLogout(accessToken);
            log.info("JwtInterceptor_checkValidAndGetUserId_mid: " + isLogout);

            // accessToken 이 유효한지 확인
            boolean checkToken = jwtConfiguration.checkToken(accessToken);
            log.info("JwtInterceptor_checkValidAndGetUserId_end: true");

            if (!isLogout && checkToken) {
                Long userId = userService.getUserIdByToken(accessToken);
                log.info("JwtInterceptor_checkValidAndGetUserId_end: true");
                return true;
            } else {
                log.info("JwtInterceptor_checkValidAndGetUserId_end: Invalid User");
                return false;
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
