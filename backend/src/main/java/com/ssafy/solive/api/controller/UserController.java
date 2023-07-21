package com.ssafy.solive.api.controller;

import com.ssafy.solive.api.request.UserRegistPostReq;
import com.ssafy.solive.api.service.UserService;
import com.ssafy.solive.common.model.CommonResponse;
import com.ssafy.solive.db.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    public CommonResponse<?> regist(@RequestBody UserRegistPostReq registInfo) {
        User user = userService.registUser(registInfo);

        if (user != null) {
            return CommonResponse.success("success");
        } else {
            // TODO: code data 추가 요망
            return CommonResponse.fail(null, "fail");
        }
    }
}
