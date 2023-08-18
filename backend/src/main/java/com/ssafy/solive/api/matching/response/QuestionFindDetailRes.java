package com.ssafy.solive.api.matching.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

/**
 * 문제 상세 조회 API에 대한 Response
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
    List<String> path;

    // 문제 분류(마스터코드)
    String masterCodeName;

    // 문제 대분류(마스터코드)
    String masterCodeCategory;

    // 문제 등록 시간
    String createTime;

    // 문제 매칭 상태
    String state;

    public QuestionFindDetailRes() {

    }

    // Querydsl을 위한 생성자
    public QuestionFindDetailRes(String userNickname, String title, String description,
        String masterCodeName, LocalDateTime createTime, Integer state) {
        this.userNickname = userNickname;
        this.title = title;
        this.description = description;
        this.masterCodeName = masterCodeName;
        this.createTime = createTime.toString();

        // 문제가 매칭 되었으면 2, 강사들의 지원이 있는 상태면 1, 문제가 등록만 된 초기 상태면 0
        if (state.equals(0)) {
            this.state = "등록됨";
        } else if (state.equals(1)) {
            this.state = "요청됨";
        } else {
            this.state = "완료됨";
        }
    }
}
