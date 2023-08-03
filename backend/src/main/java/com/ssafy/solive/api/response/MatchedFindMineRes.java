package com.ssafy.solive.api.response;

import java.time.LocalDateTime;
import lombok.Data;

/*
 *  마이페이지에서 자신이 등록한 문제 검색 API Response
 */
@Data
public class MatchedFindMineRes {

    // 각 Question들의 id(PK). 상세 조회할 때 api 요청하기 위해 필요
    Long questionId;

    // 문제 이미지
    String imagePathName;

    // 문제 제목
    String title;

    // 문제 등록 시간
    String createTime;

    // 문제 마스터코드 분류명
    String masterCodeName;

    // 문제 매칭 상태
    Integer matchingState;

    public MatchedFindMineRes() {

    }

    // Querydsl을 위한 생성자
    public MatchedFindMineRes(Long questionId, String title, LocalDateTime createTime,
        String masterCodeName, Integer matchingState) {
        this.questionId = questionId;
        this.title = title;
        this.createTime = createTime.toString();
        this.masterCodeName = masterCodeName;
        this.matchingState = matchingState;
    }
}
