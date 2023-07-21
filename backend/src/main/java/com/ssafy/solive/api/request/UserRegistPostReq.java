package com.ssafy.solive.api.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserRegistPostReq {
    String loginId;
    String loginPassword;
    String name;
    String email;
    String pictureUrl;
    String pictureName;
    String introduce;
    int gender;
}
