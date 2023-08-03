package com.ssafy.solive.api.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfilePostRes {

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
