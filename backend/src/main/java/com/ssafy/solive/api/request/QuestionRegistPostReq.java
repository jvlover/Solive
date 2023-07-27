package com.ssafy.solive.api.request;

import lombok.Data;

@Data
public class QuestionRegistPostReq {

    Long studentId;

    Integer masterCodeId;

    String title;

    String description;
}
