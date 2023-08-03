package com.ssafy.solive.api.controller;

import com.ssafy.solive.api.request.TeacherRatePostReq;
import com.ssafy.solive.api.request.UserLoginPostReq;
import com.ssafy.solive.api.request.UserModifyPasswordPutReq;
import com.ssafy.solive.api.request.UserModifyProfilePutReq;
import com.ssafy.solive.api.request.UserRegistPostReq;
import com.ssafy.solive.api.response.UserLoginPostRes;
import com.ssafy.solive.api.response.UserPrivacyPostRes;
import com.ssafy.solive.api.response.UserProfilePostRes;
import com.ssafy.solive.api.service.UserService;
import com.ssafy.solive.common.exception.UserNotFoundException;
import com.ssafy.solive.common.model.CommonResponse;
import com.ssafy.solive.db.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@CrossOrigin("*")
public class UserController {

    private static final String SUCCESS = "success";  // API 성공 시 return
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    /**
     * 유저 회원가입
     *
     * @param registInfo 로그인 정보,
     */
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

    /**
     * 유저 프로필 정보
     *
     * @param request access-token 이 들어있는 request
     * @return UserProfilePostRes
     */
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

    /**
     * 유저 개인정보
     *
     * @param request access-token이 들어있는 request
     * @return UserPrivacyPostRes
     */
    @GetMapping("/privacy")
    public CommonResponse<?> getUserPrivacy(HttpServletRequest request) {
        String accessToken = request.getHeader("access-token");
        Long userId = userService.getUserIdByAccessToken(accessToken);

        UserPrivacyPostRes userPrivacy = userService.getUserPrivacyByUserId(userId);
        if (userPrivacy != null) {
            return CommonResponse.success(userPrivacy);
        } else {
            // TODO: Exception 처리
            return null;
        }
    }

    /**
     * 로그인
     *
     * @param loginInfo 로그인 id, password
     * @return 토큰 및 상단바에 필요한 nickname 등 제공
     */
    @PostMapping("/login")
    public CommonResponse<?> login(@RequestBody UserLoginPostReq loginInfo) {
        log.info("UserController_login_start: " + loginInfo.toString());
        try {
            UserLoginPostRes userLoginPostRes = userService.loginAndGetTokens(loginInfo);
            // TODO: null이 아니라 PasswordMismatchException() 일때를 구현해야함
            if (userLoginPostRes != null) {
                return CommonResponse.success(userLoginPostRes);
            } else {
                return CommonResponse.fail(null, "Login Fail");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResponse.fail(null, "Login Exception");
        }
    }

    /**
     * 유저 프로필 수정
     *
     * @param userInfo UserModifyProfilePutReq
     * @param request  access-token 이 들어있는 request
     */
    @PutMapping()
    public CommonResponse<?> modifyProfile(@RequestBody UserModifyProfilePutReq userInfo,
        HttpServletRequest request) {
        String accessToken = request.getHeader("access-token");
        Long userId = userService.getUserIdByAccessToken(accessToken);

        try {
            userService.modifyUserProfile(userId, userInfo);
            return CommonResponse.success(SUCCESS);
        } catch (Exception e) {
            // TODO: Exception 처리
            return null;
        }
    }

    /**
     * 비밀번호 수정
     *
     * @param passwords 현재비밀번호, 바꿀비밀번호 정보
     * @param request   access-token 이 들어있는 request
     */
    @PutMapping("/password")
    public CommonResponse<?> modifyPassword(@RequestBody UserModifyPasswordPutReq passwords,
        HttpServletRequest request) {
        String accessToken = request.getHeader("access-token");
        Long userId = userService.getUserIdByAccessToken(accessToken);

        try {
            userService.modifyUserPassword(userId, passwords);
            return CommonResponse.success(SUCCESS);
        } catch (Exception e) {
            // TODO: Exception 처리
            return null;
        }
    }

    /**
     * 회원 탈퇴, User DB에 deletedAt 값 추가
     *
     * @param request accessToken 의 userId를 받기 위한 request
     */
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

    /**
     * 임시라서 없어질 듯
     *
     * @param code    code
     * @param request request
     * @return
     */
    @PutMapping("/setcode")
    public CommonResponse<?> setCode(Integer code, HttpServletRequest request) {
        String accessToken = request.getHeader("access-token");
        Long userId = userService.getUserIdByAccessToken(accessToken);

        userService.setCode(userId, code);

        return CommonResponse.success(SUCCESS);
        // TODO: Exception 처리
    }

    /**
     * 학생 Solve Point 충전
     *
     * @param solvePoint 충전 금액
     * @param request    access-token 이 들어있는 request
     */
    @PutMapping("/charge")
    public CommonResponse<?> chargeSolvePoint(Integer solvePoint, HttpServletRequest request) {
        String accessToken = request.getHeader("access-token");
        Long userId = userService.getUserIdByAccessToken(accessToken);

        userService.chargeSolvePoint(userId, solvePoint);

        return CommonResponse.success(SUCCESS);
        // TODO: Exception 처리
    }

    /**
     * 강사 Solve Point 출금
     *
     * @param solvePoint 출금 금액
     * @param request    access-token 이 들어있는 request
     */
    @PutMapping("/cashout")
    public CommonResponse<?> cashoutSolvePoint(Integer solvePoint, HttpServletRequest request) {
        String accessToken = request.getHeader("access-token");
        Long userId = userService.getUserIdByAccessToken(accessToken);

        userService.cashOutSolvePoint(userId, solvePoint);

        return CommonResponse.success(SUCCESS);
        // TODO: Exception 처리
    }

    /**
     * 학생의 강사 평점 입력
     *
     * @param ratingInfo 입력한 평점
     */
    @PutMapping("/rate")
    public CommonResponse<?> rateTeacher(@RequestBody TeacherRatePostReq ratingInfo) {
        userService.rateTeacher(ratingInfo);

        return CommonResponse.success(SUCCESS);
    }
}
