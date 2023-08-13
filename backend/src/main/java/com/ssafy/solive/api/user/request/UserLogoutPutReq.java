package com.ssafy.solive.api.user.request;

import lombok.Data;

@Data
public class UserLogoutPutReq {

    Long userId; // 로그아웃 할 userId
}
