package com.ssafy.solive.api.response;

import java.time.LocalDateTime;
import lombok.Data;

/*
 *  문제 검색 API에 대한 Response
 */
@Data
public class QuestionFindConditionRes {

    // 각 Question들의 id(PK). 상세 조회할 때 api 요청하기 위해 필요
    Long questionId;

    // 문제 등록한 유저 이름
    String userNickname;

    // 문제 이미지
    String imagePathName;

    // 문제 제목
    String title;

    // 문제 등록 시간
    String createTime;

    // 문제 마스터코드 분류명
    String masterCodeName;

    public QuestionFindConditionRes() {

    }

    // Querydsl을 위한 생성자
    public QuestionFindConditionRes(Long questionId, String userNickname, String imagePathName,
        String title,
        LocalDateTime createTime, String masterCodeName) {
        this.questionId = questionId;
        this.userNickname = userNickname;
        this.imagePathName = imagePathName;
        this.title = title;
        this.createTime = createTime.toString();
        this.masterCodeName = masterCodeName;
    }
}
