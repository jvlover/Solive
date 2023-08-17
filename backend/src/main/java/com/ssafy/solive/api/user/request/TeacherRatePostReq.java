package com.ssafy.solive.api.user.request;

import lombok.Data;

@Data
public class TeacherRatePostReq {

    Long applyId; // 해당 Matching 의 Apply id
    Integer rating; // 입력된 평점
}
