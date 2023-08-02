package com.ssafy.solive.api.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLoginPostRes {

    String accessToken;
    String refreshToken;
    String loginId;
    String masterCodeName;
    String nickname;
}
