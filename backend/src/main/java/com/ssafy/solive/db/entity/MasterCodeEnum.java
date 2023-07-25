package com.ssafy.solive.db.entity;

public enum MasterCodeEnum {
    // USER_TYPE (0 ~ 100)
    USER_TYPE_DEFAULT(0, "인증되지 않은 기본회원"),
    USER_TYPE_STUDENT(1, "학생"),
    USER_TYPE_TEACHER(2, "강사"),
    USER_TYPE_ADMIN(3, "운영자"),

    // COURSE (1,000 ~ 1,999)

    // COURSE_MATH(수학) (1,000 ~ 1,099)
    // 수학1 (1,000 ~ 1,009)
    COURSE_MATH_MATH1_LOG(1_000, "지수로그"),
    COURSE_MATH_MATH1_TRIGONOMETRIC(1_001, "삼각함수"),
    COURSE_MATH_MATH1_SEQUENCE(1_002, "극한"),

    // 수학2 (1,010 ~ 1,019)
    COURSE_MATH_MATH2_LIMIT(1_010, "극한"),
    COURSE_MATH_MATH2_DIFFERENTIAL(1_011, "미분"),
    COURSE_MATH_MATH2_INTEGRAL(1_012, "적분"),

    // 확률과 통계 (1,020 ~ 1,029)
    COURSE_MATH_STATISTICS_PROBABILITY(1_020, "확률"),
    COURSE_MATH_STATISTICS_STATISTICS(1_021, "통계"),

    // 기하 (1,030 ~ 1,039)
    COURSE_MATH_GEOMETRY_QUADRATIC(1_030, "이차곡선"),
    COURSE_MATH_GEOMETRY_VECTOR(1_031, "벡터"),
    COURSE_MATH_GEOMETRY_SPATIAL(1_032, "공간도형"),

    // 기타수학 (1,040)
    COURSE_MATH_ETC(1_040, "기타수학"),


    // COURSE_SCIENCE(과학) (1,100 ~ 1,199)
    COURSE_SCIENCE_ETC(1_100, "과학"),


    // BOARD(게시판) (2,000 ~ 2,999)
    // BOARD_NUM(2_000 ~ 2_099)
    BOARD_NUM_ANNOUNCE(2_000, "공지사항"),

    // BOARD_TYPE(2_100 ~ 2_199)
    BOARD_TYPE_PICTURE_NO(2_100, "사진 없는 게시글"),
    BOARD_TYPE_PICTURE_YES(2_101, "사진 있는 게시글");

    private final int codeValue;
    private final String codeName;

    MasterCodeEnum(int codeValue, String codeName) {
        this.codeValue = codeValue;
        this.codeName = codeName;
    }

    public int getCodeValue() {
        return codeValue;
    }
}
