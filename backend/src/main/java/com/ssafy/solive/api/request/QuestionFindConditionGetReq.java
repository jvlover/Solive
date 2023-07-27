package com.ssafy.solive.api.request;

import lombok.Getter;

@Getter
public class QuestionFindConditionGetReq {

    // TODO: 검색 조건에 관해서 프론트와 협의 필요. 무엇을 어떤 형식으로 주고 받을 것인지

    int masterCodeMiddle;

    int masterCodeLow;

    String keyword;

    String sort;
}
