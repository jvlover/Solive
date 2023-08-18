package com.ssafy.solive.api.user.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLoginPostRes {

    Long userId;
    String accessToken;
    String refreshToken;
    Integer masterCodeId;
    String nickname;
    Integer solvePoint;
    String path; // 프로필 사진 경로
}
