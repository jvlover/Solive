package com.ssafy.solive.api.response;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class QuestionFindDetailRes {

    String userNickname;

    String title;

    String description;

    String imagePathName;

    Integer masterCodeId;

    String createTime;

    public QuestionFindDetailRes() {

    }

    public QuestionFindDetailRes(String userNickname, String title, String description,
        String imagePathName, Integer masterCodeId, LocalDateTime createTime) {
        this.userNickname = userNickname;
        this.title = title;
        this.description = description;
        this.imagePathName = imagePathName;
        this.masterCodeId = masterCodeId;
        this.createTime = createTime.toString();
    }
}
