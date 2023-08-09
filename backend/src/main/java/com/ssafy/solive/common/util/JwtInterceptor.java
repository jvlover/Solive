package com.ssafy.solive.common.util;

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

    private JwtConfiguration jwtConfiguration;

    public JwtInterceptor() {
    }

    @Autowired
    public JwtInterceptor(JwtConfiguration jwtConfiguration) {
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
        log.info("==================== Jwt_Interceptor_start ====================");
        try {
            return jwtConfiguration.checkToken(request.getHeader("access-token"));
        } catch (ExpiredJwtException e) {
            throw new JwtTokenExpiredException();
        } catch (Exception e) {
            throw new JwtInvalidException();
        }
    }
}
