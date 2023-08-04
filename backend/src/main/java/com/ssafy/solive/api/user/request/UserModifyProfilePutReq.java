package com.ssafy.solive.api.user.request;

import lombok.Data;

@Data
public class UserModifyProfilePutReq {

    String pictureUrl;
    String pictureName;
    String fileName;
    String pathName;
    String contentType;
    String nickname;
    Integer gender;
    Long experience;
    String introduce;
}
