package com.ssafy.solive.api.matching.request;

import lombok.Data;

/**
 * 문제 등록 API에 대한 Request
 */
@Data
public class QuestionRegistPostReq {

    // 문제 등록한 유저(학생) id
    Long studentId;

    // 마스터코드 과목
    Integer subject;

    // 마스터코드 중분류
    Integer subSubject;

    // 마스터코드 상세분류
    Integer detail;

    // 문제 제목
    String title;

    // 문제 설명
    String description;
}
