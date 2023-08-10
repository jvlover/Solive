package com.ssafy.solive.api.matching.response;

import com.ssafy.solive.db.entity.User;
import lombok.Builder;
import lombok.Data;

/**
 * 서비스에서 DB에 지원 신청 Regist 후 컨트롤러의 해당 API 메소드에 보낼 Response DTO
 */
@Data
@Builder
public class ApplyRegistPostRes {

    // 문제 풀이 지원 신청이 왔다는 알림을 받을 학생
    User user;

    // 알림 내용, 문제의 제목으로 일단 할 것
    String questionTitle;
}
