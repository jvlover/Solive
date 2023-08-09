package com.ssafy.solive.api.user.service;

import com.ssafy.solive.api.user.request.*;
import com.ssafy.solive.api.user.response.UserLoginPostRes;
import com.ssafy.solive.api.user.response.UserPrivacyPostRes;
import com.ssafy.solive.api.user.response.UserProfilePostRes;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    public void registUser(UserRegistPostReq registInfo);

    public UserLoginPostRes loginAndGetTokens(UserLoginPostReq loginInfo);

    void logout(Long userId);

    public Long getUserIdByToken(String token);

    public String recreateAccessToken(Long userId, String refreshToken);

    public UserProfilePostRes getUserProfileByUserId(Long userId);

    UserPrivacyPostRes getUserPrivacyByUserId(Long userId);

    void modifyUserProfile(Long userId, UserModifyProfilePutReq userInfo,
                           List<MultipartFile> profilePicture);

    void modifyUserPassword(Long userId, UserModifyPasswordPutReq userInfo);

    void deleteUser(Long userId);

    void chargeSolvePoint(Long userId, Integer solvePoint);

    void cashOutSolvePoint(Long userId, Integer solvePoint);

    void rateTeacher(TeacherRatePostReq ratingInfo);

    void addFavorite(Long studentId, Long teacherId);

    void deleteFavorite(Long studentId, Long teacherId);

    boolean isLogout(String accessToken);
}
