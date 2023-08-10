package com.ssafy.solive.api.matching.request;

import lombok.Data;

/**
 * 강사가 학생이 열은 강의 세션에 입장하여 강의가 시작되는 API에 대한 Request
 */
@Data
public class MatchedPutReq {

    // 강의 세션 Id
    String sessionId;
}
