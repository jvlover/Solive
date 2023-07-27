package com.ssafy.solive.api.request;

import lombok.Data;

@Data
public class QuestionModifyPutReq {

    Long questionId;

    Long studentId;

    Integer masterCodeId;

    String title;

    String description;
}
