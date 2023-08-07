package com.ssafy.solive.api.user.service;

import com.ssafy.solive.api.user.request.TeacherRatePostReq;
import com.ssafy.solive.api.user.request.UserLoginPostReq;
import com.ssafy.solive.api.user.request.UserModifyPasswordPutReq;
import com.ssafy.solive.api.user.request.UserModifyProfilePutReq;
import com.ssafy.solive.api.user.request.UserRegistPostReq;
import com.ssafy.solive.api.user.response.UserLoginPostRes;
import com.ssafy.solive.api.user.response.UserPrivacyPostRes;
import com.ssafy.solive.api.user.response.UserProfilePostRes;
import com.ssafy.solive.db.entity.User;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    public User registUser(UserRegistPostReq registInfo);

    public UserLoginPostRes loginAndGetTokens(UserLoginPostReq loginInfo);

    void logout(Long userId);

    public Long getUserIdByAccessToken(String token);

    public UserProfilePostRes getUserProfileByUserId(Long userId);

    UserPrivacyPostRes getUserPrivacyByUserId(Long userId);

    void modifyUserProfile(Long userId, UserModifyProfilePutReq userInfo,
        MultipartFile profilePicture);

    void modifyUserPassword(Long userId, UserModifyPasswordPutReq userInfo);

    void deleteUser(Long userId);

    void setCode(Long userId, Integer code);

    void chargeSolvePoint(Long userId, Integer solvePoint);

    void cashOutSolvePoint(Long userId, Integer solvePoint);

    void rateTeacher(TeacherRatePostReq ratingInfo);

    void addFavorite(Long studentId, Long teacherId);

    void deleteFavorite(Long studentId, Long teacherId);
}
