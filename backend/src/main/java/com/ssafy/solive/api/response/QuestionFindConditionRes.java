package com.ssafy.solive.api.response;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class QuestionFindConditionRes {

    String userNickname;

    String imagePathName;

    String title;

    String createTime;

    public QuestionFindConditionRes() {

    }

    public QuestionFindConditionRes(String userNickname, String imagePathName, String title,
        LocalDateTime createTime) {
        this.userNickname = userNickname;
        this.imagePathName = imagePathName;
        this.title = title;
        this.createTime = createTime.toString();
    }
}
