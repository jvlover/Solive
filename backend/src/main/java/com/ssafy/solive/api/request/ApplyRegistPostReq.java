package com.ssafy.solive.api.request;

import lombok.Data;

/*
 *  강사의 문제 풀이 지원 신청 API에 대한 Request
 */
@Data
public class ApplyRegistPostReq {

    // 지원한 강사 Id
    Long teacherId;

    // 지원한 문제 Id
    Long questionId;

    // 강사가 예측한 문제 푸는 데에 걸리는 시간
    Integer estimatedTime;

    // 강사가 제시한 SP(단가)
    Integer solvePoint;
}
