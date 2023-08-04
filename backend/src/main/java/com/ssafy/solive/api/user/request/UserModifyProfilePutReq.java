package com.ssafy.solive.api.user.request;

import lombok.Data;

@Data
public class UserModifyProfilePutReq {

    String nickname;
    Integer gender;
    Long experience;
    String introduce;
}
