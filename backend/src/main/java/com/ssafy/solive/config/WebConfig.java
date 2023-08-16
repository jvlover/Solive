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
    public void addCorsMappings(CorsRegistry registry) {

        registry
            .addMapping("/board/**")  // 허용할 URL 패턴 설정
            .allowedOrigins("http://localhost:5173",
                "https://i9a107.p.ssafy.io/")  // 허용할 오리진(도메인) 설정
            .allowedMethods("GET", "POST", "PUT", "DELETE")  // 허용할 HTTP 메소드 설정
            .allowedHeaders("access-token", "Content-Type");  // 허용할 헤더 설정
        registry
            .addMapping("/user/**")
            .allowedOrigins("http://localhost:5173", "https://i9a107.p.ssafy.io/")
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .allowedHeaders("access-token", "Content-Type");
        registry
            .addMapping("/apply/**")
            .allowedOrigins("http://localhost:5173", "https://i9a107.p.ssafy.io/")
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .allowedHeaders("access-token", "Content-Type");
        registry
            .addMapping("/matched/**")
            .allowedOrigins("http://localhost:5173", "https://i9a107.p.ssafy.io/")
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .allowedHeaders("access-token", "Content-Type");
        registry
            .addMapping("/notification/**")
            .allowedOrigins("http://localhost:5173", "https://i9a107.p.ssafy.io/")
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .allowedHeaders("access-token", "Content-Type", "Last-Event-ID");
        registry
            .addMapping("/question/**")
            .allowedOrigins("http://localhost:5173", "https://i9a107.p.ssafy.io/")
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .allowedHeaders("access-token", "Content-Type");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(jwtInterceptor)
            .addPathPatterns("/**")
            .excludePathPatterns("/user/auth/**", "/board/auth/**", "/notification/auth/**");
    }
}