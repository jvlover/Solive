package com.ssafy.solive.api.request;

import lombok.Data;

/*
 *  문제 삭제 API에 대한 Request
 */
@Data
public class QuestionDeletePutReq {

    // 삭제할 문제 id
    Long questionId;

    // 문제 등록한 학생 id. DB에 등록된 것과 값이 같아야 함
    Long studentId;
}
