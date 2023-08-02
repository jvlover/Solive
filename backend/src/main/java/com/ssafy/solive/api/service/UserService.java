package com.ssafy.solive.api.service;

import com.ssafy.solive.api.request.TeacherRatePostReq;
import com.ssafy.solive.api.request.UserLoginPostReq;
import com.ssafy.solive.api.request.UserModifyPutReq;
import com.ssafy.solive.api.request.UserRegistPostReq;
import com.ssafy.solive.api.response.UserLoginPostRes;
import com.ssafy.solive.api.response.UserPrivacyPostRes;
import com.ssafy.solive.api.response.UserProfilePostRes;
import com.ssafy.solive.db.entity.User;

public interface UserService {

    public User registUser(UserRegistPostReq registInfo);

    public UserLoginPostRes loginAndGetTokens(UserLoginPostReq loginInfo);

    public Long getUserIdByAccessToken(String token);

    public UserProfilePostRes getUserProfileByUserId(Long userId);

    UserPrivacyPostRes getUserPrivacyByUserId(Long userId);

    void modifyUser(Long userId, UserModifyPutReq userInfo);

    void deleteUser(Long userId);

    void setCode(Long userId, Integer code);

    void chargeSolvePoint(Long userId, Integer solvePoint);

    void cashOutSolvePoint(Long userId, Integer solvePoint);

    void rateTeacher(TeacherRatePostReq ratingInfo);
}
