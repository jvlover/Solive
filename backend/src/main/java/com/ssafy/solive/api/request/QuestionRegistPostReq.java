package com.ssafy.solive.api.request;

import lombok.Getter;

@Getter
public class QuestionRegistPostReq {

    Long studentId;

    Integer masterCodeId;
    
    String title;

    String description;
}
