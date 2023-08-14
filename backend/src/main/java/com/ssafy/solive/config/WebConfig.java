package com.ssafy.solive.config;

import com.ssafy.solive.common.util.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final JwtInterceptor jwtInterceptor;

    @Autowired
    public WebConfig(JwtInterceptor jwtInterceptor) {
        this.jwtInterceptor = jwtInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(jwtInterceptor)
            .addPathPatterns("/**")
            .excludePathPatterns("/user/auth/**", "/board/auth/**", "/notification/auth/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 허용할 URL 패턴 설정
            .allowedOrigins("https://i9a107.p.ssafy.io/api")  // 허용할 오리진(도메인) 설정
            .allowedMethods("GET", "POST", "PUT", "DELETE")  // 허용할 HTTP 메소드 설정
            .allowedHeaders("*");  // 허용할 헤더 설정(임시)
    }
}