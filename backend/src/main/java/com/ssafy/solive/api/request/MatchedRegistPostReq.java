package com.ssafy.solive.api.request;

import lombok.Data;

/*
 *  학생이 강사의 문제 풀이 지원 요청에 수락하여 매칭을 생성하는 API에 대한 Request
 */
@Data
public class MatchedRegistPostReq {

    // 요청 수락한 apply id
    Long applyId;

    // 학생의 id
    Long studentId;
    
}
