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
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        log.info("=====================================     JWT Interceptor Start     =====================================");
        String requestPath = request.getRequestURI();
        log.info("JwtInterceptor_preHandle_start: " + requestPath);

        // error 페이지 요청 시 jwt토큰 인터셉터 제외
        if ("/error".equals(requestPath)) {
            return true;
        }

        try {
            String accessToken = request.getHeader("access-token");
            // 로그아웃 상태가 아닌지 확인
            boolean isLogout = userService.isLogout(accessToken);
            if (isLogout) {
                log.info("JwtInterceptor_checkValidAndGetUserId_mid: isLogout: " + isLogout);
            } else {
                log.info("JwtInterceptor_checkValidAndGetUserId_mid: isLogout: " + isLogout);
            }

            // accessToken 이 유효한지 확인
            boolean checkToken = jwtConfiguration.checkToken(accessToken);
            log.info("JwtInterceptor_checkValidAndGetUserId_end: true");

            if (!isLogout && checkToken) {
                Long userId = userService.getUserIdByToken(accessToken);
                log.info("JwtInterceptor_preHandle_end: true");
                log.info("=====================================     JWT Interceptor End     =====================================");
                return true;
            } else {
                log.info("JwtInterceptor_preHandle_end: Invalid User");
                log.info("=====================================     JWT Interceptor End     =====================================");
                return false;
            }
        } catch (ExpiredJwtException e) {
            log.info("JwtInterceptor_preHandle_end: JwtTokenExpiredException");
            log.info("=====================================     JWT Interceptor End     =====================================");
            throw new JwtTokenExpiredException();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("JwtInterceptor_preHandle_end: JwtInvalidException");
            log.info("=====================================     JWT Interceptor End     =====================================");
            throw new JwtInvalidException();
        }
    }
}
