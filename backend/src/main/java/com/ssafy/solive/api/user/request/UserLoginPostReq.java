package com.ssafy.solive.api.user.request;

import lombok.Data;

/**
 * 유저 로그인 API ([POST] /api/v1/auth/login) 요청에 필요한 리퀘스트 바디 정의.
 */
@Data
public class UserLoginPostReq {

    String loginId;
    String loginPassword;
}
