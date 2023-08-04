package com.ssafy.solive.api.matching.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

/*
 *  문제 상세 조회 API에 대한 Response
 */
@Data
public class QuestionFindDetailRes {

    // 문제 등록한 유저 이름
    String userNickname;

    // 문제 제목
    String title;

    // 문제 설명
    String description;

    // 문제 이미지
    List<String> imagePathName;

    // 문제 분류(마스터코드)
    Integer masterCodeId;

    // 문제 등록 시간
    String createTime;

    public QuestionFindDetailRes() {

    }

    // Querydsl을 위한 생성자
    public QuestionFindDetailRes(String userNickname, String title, String description,
        Integer masterCodeId, LocalDateTime createTime) {
        this.userNickname = userNickname;
        this.title = title;
        this.description = description;
        this.masterCodeId = masterCodeId;
        this.createTime = createTime.toString();
    }
}
