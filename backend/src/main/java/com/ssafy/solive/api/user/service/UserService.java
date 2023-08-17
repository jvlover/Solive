package com.ssafy.solive.api.user.service;

import com.ssafy.solive.api.user.request.TeacherRatePostReq;
import com.ssafy.solive.api.user.request.UserLoginPostReq;
import com.ssafy.solive.api.user.request.UserModifyPasswordPutReq;
import com.ssafy.solive.api.user.request.UserModifyProfilePutReq;
import com.ssafy.solive.api.user.request.UserRegistPostReq;
import com.ssafy.solive.api.user.response.StudentFavoriteGetRes;
import com.ssafy.solive.api.user.response.TeacherOnlineGetRes;
import com.ssafy.solive.api.user.response.UserLoginPostRes;
import com.ssafy.solive.api.user.response.UserPrivacyPostRes;
import com.ssafy.solive.api.user.response.UserProfilePostRes;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    public void registUser(UserRegistPostReq registInfo);

    public UserLoginPostRes loginAndGetTokens(UserLoginPostReq loginInfo);

    void logout(Long userId);

    public Long getUserIdByToken(String token);

    public String recreateAccessToken(Long userId, String refreshToken);

    public UserProfilePostRes getUserProfileByUserId(Long userId);

    UserPrivacyPostRes getUserPrivacyByUserId(Long userId);

    String modifyUserProfile(Long userId, UserModifyProfilePutReq userInfo,
        List<MultipartFile> profilePicture);

    void modifyUserPassword(Long userId, UserModifyPasswordPutReq userInfo);

    void deleteUser(Long userId);

    Integer chargeSolvePoint(Long userId, Integer solvePoint);

    Integer cashOutSolvePoint(Long userId, Integer solvePoint);

    void rateTeacher(TeacherRatePostReq ratingInfo);

    void addFavorite(Long studentId, Long applyId);

    void deleteFavorite(Long studentId, Long teacherId);

    List<StudentFavoriteGetRes> findAllFavorite(Long studentId);

    boolean isLogout(String accessToken);

    List<TeacherOnlineGetRes> getOnlineTeacher();
}
