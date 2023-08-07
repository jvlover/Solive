package com.ssafy.solive.api.request;

import lombok.Data;

/*
 *  유저(학생)가 강사들의 문제 풀이 지원 신청을 검색하기 위한 API의 Request
 */
@Data
public class ApplyFindGetReq {

    // TODO: 검색 조건에 관해서 프론트와 협의 필요. 무엇을 어떤 형식으로 주고 받을 것인지

    // 학생이 어떤 문제에 대해서 검색했는지
    Long questionId;

    /*
     *  정렬 기준
     *  예상 풀이시간 순 오름차순 : TIME
     *  가격 순 오름차순 : PRICE_ASC, 내림차순 : PRICE_DESC
     *  평점 내림차순 : RATE
     */
    String sort;

    /*
     *  강사의 선호 과목과 문제의 과목 일치 여부 선택
     *  True : 선택함, False : 선택 안 함
     */
    Boolean isFavorite;

}
