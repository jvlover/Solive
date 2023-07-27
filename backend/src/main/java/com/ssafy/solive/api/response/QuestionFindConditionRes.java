package com.ssafy.solive.api.response;

import lombok.Getter;

@Getter
public class QuestionFindConditionRes {

    String userNickname;

    String imagePathName;

    String title;

    String time;

    public QuestionFindConditionRes() {

    }

    public QuestionFindConditionRes(String userNickname, String imagePathName, String title,
        String time) {
        this.userNickname = userNickname;
        this.imagePathName = imagePathName;
        this.title = title;
        this.time = time;
    }
}
