package com.ssafy.solive.api.request;

import lombok.Data;

/*
 *  문제 등록 API에 대한 Request
 */
@Data
public class QuestionRegistPostReq {

    // 문제 등록한 유저(학생) id
    Long studentId;

    // 마스터코드 id
    Integer masterCodeId;

    // 문제 제목
    String title;

    // 문제 설명
    String description;
}
