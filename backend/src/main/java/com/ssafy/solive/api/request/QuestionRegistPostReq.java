package com.ssafy.solive.api.request;

import com.ssafy.solive.db.entity.Question;
import lombok.Getter;

@Getter
public class QuestionRegistPostReq {

    int studentId;

    int masterCodeId;

    String description;

    public Question toQuestion() {
        return Question.builder()
            .studentId(studentId)
            .masterCodeId(masterCodeId)
            .description(description)
            .build();
    }
}
