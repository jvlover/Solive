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

    /**
     * accessToken이 유효하면 true, 만료됐으면 JwtTokenExpiredException, 이외는 false
     *
     * @param request  current HTTP request
     * @param response current HTTP response
     * @param handler  chosen handler to execute, for type and/or instance evaluation
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) {
        log.info("JwtInterceptor_preHandle_start");
        try {
            String accessToken = request.getHeader("access-token");
            // 로그아웃 상태가 아닌지 확인
            boolean isLogout = userService.isLogout(accessToken);
            log.info("JwtInterceptor_preHandle_mid: " + isLogout);

            // accessToken이 유효한지 확인
            boolean checkToken = jwtConfiguration.checkToken(request.getHeader("access-token"));
            log.info("JwtInterceptor_preHandle_end: true");
            return !isLogout && checkToken;
        } catch (ExpiredJwtException e) {
            log.info("JwtInterceptor_preHandle_end: false");
            throw new JwtTokenExpiredException();
        } catch (Exception e) {
            e.printStackTrace();
            log.info("JwtInterceptor_preHandle_end: false");
            throw new JwtInvalidException();
        }
    }
}
