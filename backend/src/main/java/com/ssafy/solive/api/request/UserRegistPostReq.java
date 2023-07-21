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
        User user = new User();
        user.setLoginId(loginId);
        user.setLoginPassword(loginPassword);
        user.setNickname(nickname);
        user.setEmail(email);
        user.setPictureUrl(pictureUrl);
        user.setPictureName(pictureName);
        user.setIntroduce(introduce);
        user.setGender(gender);
        return user;
    }
}
