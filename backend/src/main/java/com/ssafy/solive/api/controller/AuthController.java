package com.ssafy.solive.api.controller;

import com.ssafy.solive.api.request.UserLoginPostReq;
import com.ssafy.solive.api.service.UserService;
import com.ssafy.solive.common.model.CommonResponse;
import com.ssafy.solive.config.JwtConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 인증 관련 API 요청 처리를 위한 컨트롤러 정의.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private UserService userService;

    @Autowired
    public AuthController(UserService userService, JwtConfiguration jwtConfiguration) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public CommonResponse<?> login(@RequestBody UserLoginPostReq loginInfo) {
        try {
            String accessToken = userService.loginAndGetAccessToken(loginInfo);
            if (accessToken != null) {
                return CommonResponse.success(accessToken);
            } else {
                return CommonResponse.fail(null, "Login Fail");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResponse.fail(null, "Login Exception");
        }
    }
}
