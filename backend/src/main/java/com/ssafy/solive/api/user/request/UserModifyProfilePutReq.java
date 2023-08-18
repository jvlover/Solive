package com.ssafy.solive.api.user.request;

import lombok.Data;

@Data
public class UserModifyProfilePutReq {

    String nickname;
    String introduce;
    Integer gender;
    Integer teacherSubjectName;
}
