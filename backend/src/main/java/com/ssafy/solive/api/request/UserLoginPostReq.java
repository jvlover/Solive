package com.ssafy.solive.api.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginPostReq {

    String loginId;
    String loginPassword;
}
