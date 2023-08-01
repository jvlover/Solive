package com.ssafy.solive.api.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfilePostRes {

    Integer masterCodeId;
    String loginId;
    String nickname;
    String email;
    String pictureUrl;
    String pictureName;
    String fileName;
    String pathName;
    String contentType;
    String introduce;
    Long experience;
    LocalDateTime signinTime;
    int gender;
}
