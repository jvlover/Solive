package com.ssafy.solive.api.matching.request;

import lombok.Data;

/**
 * 문제 검색 API에 대한 조건 Request
 */
@Data
public class QuestionFindConditionGetReq {

    // 마스터코드 중분류
    Integer masterCodeMiddle;

    // 마스터코드 소분류
    Integer masterCodeLow;

    // 검색어. Null 가능
    String keyword;

    // TIME_ASC와 TIME_DESC(문제 등록 시간 오름차순, 내림차순)
    String sort;
}
