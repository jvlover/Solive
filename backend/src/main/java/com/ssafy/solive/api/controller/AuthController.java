package com.ssafy.solive.api.controller;

import com.ssafy.solive.api.request.UserLoginPostReq;
import com.ssafy.solive.api.service.UserService;
import com.ssafy.solive.common.model.CommonResponse;
import com.ssafy.solive.db.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public CommonResponse<?> login(@RequestBody UserLoginPostReq loginInfo) {
        String userLoginId = loginInfo.getLoginId();
        String userLoginPassword = loginInfo.getLoginPassword();

        User user = userService.getUserByUserId(userLoginId);
        /*
        TODO: 암호화 후 아래 코드 개선해 사용
        // 로그인 요청한 유저로부터 입력된 패스워드 와 디비에 저장된 유저의 암호화된 패스워드가 같은지 확인.(유효한 패스워드인지 여부 확인)
        if (passwordEncoder.matches(password, user.getPassword())) {
            // 유효한 패스워드가 맞는 경우, 로그인 성공으로 응답.(액세스 토큰을 포함하여 응답값 전달)
            return ResponseEntity.ok(UserLoginPostRes.of(200, "Success", JwtTokenUtil.getToken(userId)));
        }
        */
        if (user != null) {
            return CommonResponse.success("Login Success");
        } else {
            return CommonResponse.fail(null, "Login Fail");
        }
    }
}
