package com.ssafy.solive.api.response;

import lombok.Data;

/*
 *  유저(학생)가 강사들의 문제 풀이 지원 신청을 검색한 API의 결과 Response
 */
@Data
public class ApplyFindRes {

    // 지원 요청 식별
    Long applyId;

    // 강사 이름
    String teacherNickname;

    // 강사 프로필 사진
    String teacherPathName;

    // 강사의 선호 과목 마스터코드
    Integer teacherSubjectId;

    // 강사가 문제를 풀기 위해 제시한 Solve Point
    Integer solvePoint;

    // 강사가 문제를 푸는데 걸릴 것이라 예측한 시간
    Integer estimatedTime;

    // 강사의 평점 총 합
    Integer ratingSum;

    // 강사의 평점 총 개수
    Integer ratingCount;

    public ApplyFindRes() {

    }

    // Querydsl을 위한 생성자
    public ApplyFindRes(Long applyId, String teacherNickname, String teacherPathName,
        Integer teacherSubjectId,
        Integer solvePoint, Integer estimatedTime, Integer ratingSum, Integer ratingCount) {
        this.applyId = applyId;
        this.teacherNickname = teacherNickname;
        this.teacherPathName = teacherPathName;
        this.teacherSubjectId = teacherSubjectId;
        this.solvePoint = solvePoint;
        this.estimatedTime = estimatedTime;
        this.ratingSum = ratingSum;
        this.ratingCount = ratingCount;
    }
}
