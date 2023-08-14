package com.ssafy.solive.api.user.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudentFavoriteGetRes {

    String teacherSubjectName; // 선생님 선호 과목
    String path; // 선생님 프로필 사진
    String teacherNickname; // 선생님 닉네임
    Integer ratingSum; // 선생님 평점 합
    Integer ratingCount; // 선생님 평가 수
}
