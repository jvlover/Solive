package com.ssafy.solive.api.request;

import lombok.Data;

@Data
public class QuestionDeletePutReq {

    Long questionId;

    Long studentId;
}
