package com.ssafy.solive.api.request;

import lombok.Getter;
import lombok.Setter;

/**
 * 유저 회원가입 API ([POST] /api/v1/users) 요청에 필요한 리퀘스트 바디 정의.
 */
@Getter
@Setter
public class UserRegistPostReq {

    String loginId;
    String loginPassword;
    Integer masterCodeId;
    String nickname;
    String email;
    String pictureUrl;
    String pictureName;
    String introduce;
    int gender;
}
