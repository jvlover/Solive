package com.ssafy.solive.api.user.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserModifyPasswordPutReq {

    String oldPassword;
    String newPassword;
}
