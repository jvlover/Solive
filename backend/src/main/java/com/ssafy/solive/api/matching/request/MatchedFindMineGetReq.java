package com.ssafy.solive.api.matching.request;

import lombok.Data;

/**
 * 마이페이지에서 자신의 매칭 검색 API 조건 Request
 */
@Data
public class MatchedFindMineGetReq {

    // 검색하는 유저 아이디
    Long userId;

    // 유저의 상태 (학생 = 1, 강사 = 2)
    Integer userState;

    // 마스터코드 중분류
    Integer masterCodeMiddle;

    // 마스터코드 소분류
    Integer masterCodeLow;

    // 문제의 매칭 상태 필터링. 문제 전체 검색은 3, 문제가 매칭 되었으면 2, 강사들의 지원이 있는 상태면 1, 문제가 등록만 된 초기 상태면 0
    Integer matchingState;

    // 검색어. Null 가능
    String keyword;

    // TIME_ASC와 TIME_DESC(문제 등록 시간 오름차순, 내림차순)
    String sort;

    // 페이지네이션을 위한 페이지 넘버
    Integer pageNum;
}
