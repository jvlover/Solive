package com.ssafy.solive.api.request;

import com.ssafy.solive.db.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistPostReq {

    String loginId;
    String loginPassword;
    String nickname;
    String email;
    String pictureUrl;
    String pictureName;
    String introduce;
    int gender;

    public User toDto() {
        return User.builder()
            .loginId(loginId)
            .loginPassword(loginPassword)
            .nickname(nickname)
            .email(email)
            .pictureUrl(pictureUrl)
            .pictureName(pictureName)
            .introduce(introduce)
            .gender(gender)
            .build();
    }
}
