package com.ssafy.solive.api.matching.request;

import lombok.Data;

/**
 * 문제 수정 API에 대한 Request
 */
@Data
public class QuestionModifyPutReq {

    // 수정할 문제 id
    Long questionId;

    // 문제 등록한 학생 id. DB에 등록된 것과 값이 같아야 함
    Long studentId;

    // 마스터코드 id
    Integer masterCodeId;

    // 문제 제목
    String title;

    // 문제 설명
    String description;
}
