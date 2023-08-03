package com.ssafy.solive.api.request;

import lombok.Data;

/*
 *  마이페이지에서 자신의 매칭 검색 API 조건 Request
 */
@Data
public class MatchedFindMineGetReq {

    // TODO: 검색 조건에 관해서 프론트와 협의 필요. 무엇을 어떤 형식으로 주고 받을 것인지

    // 검색하는 유저 아이디
    Long userId;

    // 유저의 상태 (학생 = 1, 강사 = 2)
    Integer userState;

    // 마스터코드 중분류
    Integer masterCodeMiddle;

    // 마스터코드 소분류
    Integer masterCodeLow;

    // 문제의 매칭 상태 필터링
    Integer matchingState;

    // 검색어. Null 가능
    String keyword;

    // TIME_ASC와 TIME_DESC(문제 등록 시간 오름차순, 내림차순)
    String sort;
}