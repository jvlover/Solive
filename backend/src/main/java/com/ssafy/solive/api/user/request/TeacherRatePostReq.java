package com.ssafy.solive.api.user.request;

import lombok.Data;

@Data
public class TeacherRatePostReq {

    Long userId; // 선생님 userId
    Integer rating; // 입력된 평점
}