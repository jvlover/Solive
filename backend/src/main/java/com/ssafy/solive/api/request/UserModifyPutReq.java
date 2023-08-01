package com.ssafy.solive.api.request;

import lombok.Data;

@Data
public class UserModifyPutReq {

    String nickname;
    String email;
    String pictureUrl;
    String pictureName;
    String fileName;
    String pathName;
    String contentType;
    String introduce;
    int gender;
}
