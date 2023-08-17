package com.ssafy.solive.api.user.controller;

import com.ssafy.solive.api.user.request.TeacherRatePostReq;
import com.ssafy.solive.api.user.request.UserAddFavoritePostReq;
import com.ssafy.solive.api.user.request.UserCashOutPutReq;
import com.ssafy.solive.api.user.request.UserChargePutReq;
import com.ssafy.solive.api.user.request.UserDeleteFavoritePutReq;
import com.ssafy.solive.api.user.request.UserLoginPostReq;
import com.ssafy.solive.api.user.request.UserLogoutPutReq;
import com.ssafy.solive.api.user.request.UserModifyPasswordPutReq;
import com.ssafy.solive.api.user.request.UserModifyProfilePutReq;
import com.ssafy.solive.api.user.request.UserRecreateRefreshTokenPostReq;
import com.ssafy.solive.api.user.request.UserRegistPostReq;
import com.ssafy.solive.api.user.response.StudentFavoriteGetRes;
import com.ssafy.solive.api.user.response.TeacherOnlineGetRes;
import com.ssafy.solive.api.user.response.UserLoginPostRes;
import com.ssafy.solive.api.user.response.UserPrivacyPostRes;
import com.ssafy.solive.api.user.response.UserProfilePostRes;
import com.ssafy.solive.api.user.service.UserService;
import com.ssafy.solive.common.model.CommonResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 유저 관련 API 요청 처리를 위한 컨트롤러 정의.
 */
@Slf4j
@RestController
@RequestMapping("/user")
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
        Long userId = userService.getUserIdByToken(request.getHeader("access-token"));
        log.info("UserController_getUserProfile_start: " + userId);

        UserProfilePostRes userProfile = userService.getUserProfileByUserId(userId);
        log.info("UserController_getUserProfile_end: " + userProfile);
        return CommonResponse.success(userProfile);
    }

    /**
     * 유저 개인정보
     *
     * @param request access-token 이 들어있는 request
     * @return UserPrivacyPostRes
     */
    @GetMapping("/privacy")
    public CommonResponse<?> getUserPrivacy(HttpServletRequest request) {
        Long userId = userService.getUserIdByToken(request.getHeader("access-token"));
        log.info("UserController_getUserPrivacy_start: " + userId);

        UserPrivacyPostRes userPrivacy = userService.getUserPrivacyByUserId(userId);
        log.info("UserController_getUserPrivacy_end: " + userPrivacy);
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
        log.info("UserController_login_end: " + userLoginPostRes);
        return CommonResponse.success(userLoginPostRes);
    }

    /**
     * 로그아웃
     *
     * @param userInfo userId
     */
    @PutMapping("/auth/logout")
    public CommonResponse<?> logout(@RequestBody UserLogoutPutReq userInfo) {
        log.info("UserController_logout_start: " + userInfo.toString());
        Long userId = userInfo.getUserId();
        log.info("UserController_logout_start: " + userId);

        userService.logout(userId);
        log.info("UserController_logout_end: success");
        return CommonResponse.success(SUCCESS);
    }

    /**
     * 유저 프로필 수정
     *
     * @param userInfo UserModifyProfilePutReq
     * @param request  access-token 이 들어있는 request
     * @return 수정된 프로필 사진의 path
     */
    @PutMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public CommonResponse<?> modifyProfile(@RequestPart UserModifyProfilePutReq userInfo,
        @RequestPart(value = "files", required = false) List<MultipartFile> profilePicture,
        HttpServletRequest request) {
        Long userId = userService.getUserIdByToken(request.getHeader("access-token"));
        log.info("UserController_modifyProfile_start: " + userInfo + ", " + profilePicture + ", "
            + userId);

        String path = userService.modifyUserProfile(userId, userInfo, profilePicture);
        log.info("UserController_modifyProfile_end: " + path);
        return CommonResponse.success(path);
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
        Long userId = userService.getUserIdByToken(request.getHeader("access-token"));
        log.info("UserController_modifyPassword_start: " + passwords + ", " + userId);

        userService.modifyUserPassword(userId, passwords);
        log.info("UserController_modifyPassword_end: success");
        return CommonResponse.success(SUCCESS);
    }

    /**
     * 회원 탈퇴, User DB에 deletedAt 값 추가
     *
     * @param request accessToken 의 userId를 받기 위한 request
     */
    @PutMapping("/delete")
    public CommonResponse<?> delete(HttpServletRequest request) {
        Long userId = userService.getUserIdByToken(request.getHeader("access-token"));
        log.info("UserController_delete_start: " + userId);

        userService.deleteUser(userId);
        log.info("UserController_delete_end: success");
        return CommonResponse.success(SUCCESS);
    }

    /**
     * 학생 Solve Point 충전
     *
     * @param userChargePutReq 충전 금액
     * @param request          access-token 이 들어있는 request
     */
    @PutMapping("/charge")
    public CommonResponse<?> chargeSolvePoint(@RequestBody UserChargePutReq userChargePutReq,
        HttpServletRequest request) {
        Long userId = userService.getUserIdByToken(request.getHeader("access-token"));
        Integer solvePoint = userChargePutReq.getSolvePoint();
        log.info("UserController_chargeSolvePoint_start: " + solvePoint + ", " + userId);

        Integer newSolvePoint = userService.chargeSolvePoint(userId, solvePoint);

        log.info("UserController_chargeSolvePoint_end: " + newSolvePoint);
        return CommonResponse.success(newSolvePoint);
    }

    /**
     * 강사 Solve Point 출금
     *
     * @param userCashOutPutReq 출금 금액
     * @param request           access-token 이 들어있는 request
     */
    @PutMapping("/cashout")
    public CommonResponse<?> cashoutSolvePoint(@RequestBody UserCashOutPutReq userCashOutPutReq,
        HttpServletRequest request) {
        Long userId = userService.getUserIdByToken(request.getHeader("access-token"));
        Integer solvePoint = userCashOutPutReq.getSolvePoint();
        log.info("UserController_cashoutSolvePoint_start: " + solvePoint + ", " + userId);

        Integer newSolvePoint = userService.cashOutSolvePoint(userId, solvePoint);

        log.info("UserController_cashoutSolvePoint_end: " + newSolvePoint);
        return CommonResponse.success(newSolvePoint);
    }

    /**
     * 학생의 강사 평점 입력
     *
     * @param ratingInfo 입력한 평점
     */
    @PutMapping("/rate")
    public CommonResponse<?> rateTeacher(@RequestBody TeacherRatePostReq ratingInfo) {
        log.info("UserController_rateTeacher_start: " + ratingInfo);
        userService.rateTeacher(ratingInfo);

        log.info("UserController_rateTeacher_end: success");
        return CommonResponse.success(SUCCESS);
    }

    /**
     * accessToken 만료시, accessToken 재발급을 위한 API
     *
     * @param userRecreateRefreshTokenPostReq 재 생성을 위한 refreshToken
     * @return accessToken 재 생성된 accessToken
     */
    @PostMapping("/auth/refresh")
    public CommonResponse<?> recreateAccessToken(
        @RequestBody UserRecreateRefreshTokenPostReq userRecreateRefreshTokenPostReq) {
        String refreshToken = userRecreateRefreshTokenPostReq.getRefreshToken();
        Long userId = userService.getUserIdByToken(refreshToken);
        log.info("UserController_recreateAccessToken_start: " + userId);
        String accessToken = userService.recreateAccessToken(userId, refreshToken);

        log.info("UserController_recreateAccessToken_end: " + accessToken);
        return CommonResponse.success(accessToken);
    }

    /**
     * 학생이 선생님 즐겨찾기를 추가하는 api
     *
     * @param userAddFavoritePostReq 추가하고싶은 선생님의 Id
     * @param request                request
     * @return
     */
    @PostMapping("/favorite/add")
    public CommonResponse<?> addFavorite(@RequestBody UserAddFavoritePostReq userAddFavoritePostReq,
        HttpServletRequest request) {
        Long studentId = userService.getUserIdByToken(request.getHeader("access-token"));
        Long teacherId = userAddFavoritePostReq.getTeacherId();
        log.info("UserController_addFavorite_start: " + studentId + teacherId);

        userService.addFavorite(studentId, teacherId);

        log.info("UserController_addFavorite_end: success");
        return CommonResponse.success(SUCCESS);
    }

    /**
     * 학생이 선생님 즐겨찾기를 삭제하는 api
     *
     * @param userDeleteFavoritePutReq 삭제하고 싶은 선생님의 Id
     * @param request                  request
     * @return
     */
    @PutMapping("/favorite/delete")
    public CommonResponse<?> deleteFavorite(
        @RequestBody UserDeleteFavoritePutReq userDeleteFavoritePutReq,
        HttpServletRequest request) {
        Long studentId = userService.getUserIdByToken(request.getHeader("access-token"));
        Long teacherId = userDeleteFavoritePutReq.getTeacherId();
        log.info("UserController_deleteFavorite_start: " + studentId + teacherId);

        userService.deleteFavorite(studentId, teacherId);
        log.info("UserController_deleteFavorite_end: success");
        return CommonResponse.success(SUCCESS);
    }

    /**
     * 학생이 선생님 즐겨찾기 목록을 불러오는 api
     *
     * @param request request
     * @return studentFavoriteGetResList
     */
    @GetMapping("/favorite")
    public CommonResponse<?> findAllFavorite(HttpServletRequest request) {
        Long studentId = userService.getUserIdByToken(request.getHeader("access-token"));
        log.info("UserController_findAllFavorite_start: " + studentId);

        List<StudentFavoriteGetRes> studentFavoriteGetResList = userService.findAllFavorite(
            studentId);

        log.info("UserController_findAllFavorite_end: " + studentFavoriteGetResList);
        return CommonResponse.success(studentFavoriteGetResList);
    }

    /**
     * 유저 main page 를 접속할 떄, 온라인인 선생님 중, 평점 순으로 Get
     *
     * @return 평점 높은 3명의 선생님 List
     */
    @GetMapping("/onlineteacher")
    public CommonResponse<?> getOnlineTeacher() {
        log.info("UserController_getOnlineTeacher_start");

        List<TeacherOnlineGetRes> teacherList = userService.getOnlineTeacher();
        log.info("UserController_getOnlineTeacher_end: " + teacherList);
        return CommonResponse.success(teacherList);
    }
}
