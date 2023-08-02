package com.ssafy.solive.api.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserModifyPasswordPutReq {

    String oldPassword;
    String newPassword;
}
