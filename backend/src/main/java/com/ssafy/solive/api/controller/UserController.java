package com.ssafy.solive.api.controller;

import com.ssafy.solive.api.request.UserRegistPostReq;
import com.ssafy.solive.api.service.UserService;
import com.ssafy.solive.common.exception.UserNotFoundException;
import com.ssafy.solive.common.model.CommonResponse;
import com.ssafy.solive.db.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
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
}
