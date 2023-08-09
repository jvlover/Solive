package com.ssafy.solive.api.user.controller;

import com.ssafy.solive.api.user.request.*;
import com.ssafy.solive.api.user.response.UserLoginPostRes;
import com.ssafy.solive.api.user.response.UserPrivacyPostRes;
import com.ssafy.solive.api.user.response.UserProfilePostRes;
import com.ssafy.solive.api.user.service.UserService;
import com.ssafy.solive.common.model.CommonResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    @PostMapping("/auth")
    public CommonResponse<?> regist(@RequestBody UserRegistPostReq registInfo) {
        log.info("UserController_regist_start: " + registInfo.toString());
        userService.registUser(registInfo);

        log.info("UserController_regist_end: success");
        return CommonResponse.success(SUCCESS);
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
        Long userId = userService.getUserIdByToken(accessToken);

        UserProfilePostRes userProfile = userService.getUserProfileByUserId(userId);
        log.info("UserController_getUserProfile_end: " + userProfile);
        return CommonResponse.success(userProfile);
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
        Long userId = userService.getUserIdByToken(accessToken);

        UserPrivacyPostRes userPrivacy = userService.getUserPrivacyByUserId(userId);
        return CommonResponse.success(userPrivacy);
    }

    /**
     * 로그인
     *
     * @param loginInfo 로그인 id, password
     * @return 토큰 및 상단바에 필요한 nickname 등 제공
     */
    @PostMapping("/auth/login")
    public CommonResponse<?> login(@RequestBody UserLoginPostReq loginInfo) {
        log.info("UserController_login_start: " + loginInfo.toString());
        UserLoginPostRes userLoginPostRes = userService.loginAndGetTokens(loginInfo);
        return CommonResponse.success(userLoginPostRes);
    }

    @PutMapping("/logout")
    public CommonResponse<?> logout(HttpServletRequest request) {
        String accessToken = request.getHeader("access-token");
        Long userId = userService.getUserIdByToken(accessToken);

        // TODO: Token 관련 처리해야함!!!
        userService.logout(userId);
        return CommonResponse.success(SUCCESS);
    }

    /**
     * 유저 프로필 수정
     *
     * @param userInfo UserModifyProfilePutReq
     * @param request  access-token 이 들어있는 request
     */
    @PutMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public CommonResponse<?> modifyProfile(@RequestPart UserModifyProfilePutReq userInfo,
                                           @RequestPart(value = "files", required = false) List<MultipartFile> profilePicture,
                                           HttpServletRequest request) {
        String accessToken = request.getHeader("access-token");
        Long userId = userService.getUserIdByToken(accessToken);

        userService.modifyUserProfile(userId, userInfo, profilePicture);
        return CommonResponse.success(SUCCESS);

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
        Long userId = userService.getUserIdByToken(accessToken);

        userService.modifyUserPassword(userId, passwords);
        return CommonResponse.success(SUCCESS);
    }

    /**
     * 회원 탈퇴, User DB에 deletedAt 값 추가
     *
     * @param request accessToken 의 userId를 받기 위한 request
     */
    @PutMapping("/delete")
    public CommonResponse<?> delete(HttpServletRequest request) {
        String accessToken = request.getHeader("access-token");
        Long userId = userService.getUserIdByToken(accessToken);

        userService.deleteUser(userId);
        return CommonResponse.success(SUCCESS);
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
        Long userId = userService.getUserIdByToken(accessToken);

        userService.chargeSolvePoint(userId, solvePoint);

        return CommonResponse.success(SUCCESS);
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
        Long userId = userService.getUserIdByToken(accessToken);

        userService.cashOutSolvePoint(userId, solvePoint);

        return CommonResponse.success(SUCCESS);
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

    /**
     * accessToken 만료시, accessToken 재발급을 위한 API
     *
     * @param refreshToken 재 생성을 위한 refreshToken
     * @return accessToken 재 생성된 accessToken
     */
    @PostMapping("/refresh")
    public CommonResponse<?> recreateAccessToken(String refreshToken) {
        Long userId = userService.getUserIdByToken(refreshToken);
        String accessToken = userService.recreateAccessToken(userId, refreshToken);

        log.info("UserController_recreateAccessToken_end: accessToken: " + accessToken);
        return CommonResponse.success(accessToken);
    }

    @PostMapping("/favorite/add")
    public CommonResponse<?> addFavorite(@RequestBody Long teacherId,
                                         HttpServletRequest request) {
        String accessToken = request.getHeader("access-token");
        Long studentId = userService.getUserIdByToken(accessToken);

        userService.addFavorite(studentId, teacherId);

        return CommonResponse.success(SUCCESS);
    }

    @PutMapping("/favorite/delete")
    public CommonResponse<?> deleteFavorite(@RequestBody Long teacherId,
                                            HttpServletRequest request) {
        String accessToken = request.getHeader("access-token");
        Long studentId = userService.getUserIdByToken(accessToken);

        userService.deleteFavorite(studentId, teacherId);
        return CommonResponse.success(SUCCESS);
    }
}
