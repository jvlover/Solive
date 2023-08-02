package com.ssafy.solive.api.controller;

import com.ssafy.solive.api.request.UserLoginPostReq;
import com.ssafy.solive.api.request.UserModifyPutReq;
import com.ssafy.solive.api.request.UserRegistPostReq;
import com.ssafy.solive.api.response.UserLoginPostRes;
import com.ssafy.solive.api.response.UserProfilePostRes;
import com.ssafy.solive.api.service.UserService;
import com.ssafy.solive.common.exception.UserNotFoundException;
import com.ssafy.solive.common.model.CommonResponse;
import com.ssafy.solive.db.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 유저 관련 API 요청 처리를 위한 컨트롤러 정의.
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    private static final String SUCCESS = "success";  // API 성공 시 return
    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping()
    public CommonResponse<?> regist(@RequestBody UserRegistPostReq registInfo) {
        log.info("UserController_regist_start: " + registInfo.toString());
        User user = userService.registUser(registInfo);

        if (user != null) {
            log.info("UserController_regist_end: success");
            return CommonResponse.success(SUCCESS);
        } else {
            log.info("UserController_regist_end: UserNotFoundException");
            throw new UserNotFoundException();
        }
    }

    @GetMapping()
    public CommonResponse<?> getUserProfile(HttpServletRequest request) {
        String accessToken = request.getHeader("access-token");
        Long userId = userService.getUserIdByAccessToken(accessToken);

        UserProfilePostRes userProfile = userService.getUserProfileByUserId(userId);
        log.info("UserController_getUserProfile_end: " + userProfile);
        if (userProfile != null) {
            return CommonResponse.success(userProfile);
        } else {
            // TODO: Exception 처리
            return null;
        }
    }

    @PostMapping("/login")
    public CommonResponse<?> login(@RequestBody UserLoginPostReq loginInfo) {
        log.info("UserController_login_start: " + loginInfo.toString());
        try {
            UserLoginPostRes Tokens = userService.loginAndGetTokens(loginInfo);
            // TODO: null이 아니라 PasswordMismatchException() 일때를 구현해야함
            if (Tokens != null) {
                return CommonResponse.success(Tokens);
            } else {
                return CommonResponse.fail(null, "Login Fail");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResponse.fail(null, "Login Exception");
        }
    }

    @PutMapping()
    public CommonResponse<?> modify(@RequestBody UserModifyPutReq userInfo,
        HttpServletRequest request) {
        String accessToken = request.getHeader("access-token");
        Long userId = userService.getUserIdByAccessToken(accessToken);

        try {
            userService.modifyUser(userId, userInfo);
            return CommonResponse.success(SUCCESS);
        } catch (Exception e) {
            // TODO: Exception 처리
            return null;
        }
    }

    @PutMapping("/delete")
    public CommonResponse<?> delete(HttpServletRequest request) {
        try {
            String accessToken = request.getHeader("access-token");
            Long userId = userService.getUserIdByAccessToken(accessToken);

            userService.deleteUser(userId);
            return CommonResponse.success(SUCCESS);
        } catch (Exception e) {
            // TODO: Exception 처리
            e.printStackTrace();
            return null;
//            return new RuntimeException();
        }
    }

    @PutMapping("/setcode")
    public CommonResponse<?> setCode(Integer code, HttpServletRequest request) {
        String accessToken = request.getHeader("access-token");
        Long userId = userService.getUserIdByAccessToken(accessToken);

        userService.setCode(userId, code);

        return CommonResponse.success(SUCCESS);
        // TODO: Exception 처리
    }

    @PutMapping("/charge")
    public CommonResponse<?> chargeSolvePoint(Integer solvePoint, HttpServletRequest request) {
        String accessToken = request.getHeader("access-token");
        Long userId = userService.getUserIdByAccessToken(accessToken);

        userService.chargeSolvePoint(userId, solvePoint);

        return CommonResponse.success(SUCCESS);
        // TODO: Exception 처리
    }
}
