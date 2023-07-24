package com.ssafy.solive.api.request;

import com.ssafy.solive.db.entity.User;
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

    public User toUser() {
        return User.builder()
            .loginId(loginId)
            .loginPassword(loginPassword)
            .masterCodeId(masterCodeId)
            .nickname(nickname)
            .email(email)
            .pictureUrl(pictureUrl)
            .pictureName(pictureName)
            .introduce(introduce)
            .gender(gender)
            .build();
    }
}
