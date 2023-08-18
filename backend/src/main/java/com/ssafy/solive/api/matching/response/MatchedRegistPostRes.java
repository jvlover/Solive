package com.ssafy.solive.api.matching.response;

import com.ssafy.solive.db.entity.User;
import lombok.Builder;
import lombok.Data;

/**
 * 서비스에서 DB에 매칭 Regist 후 컨트롤러의 해당 API 메소드에 보낼 Response DTO
 */
@Data
@Builder
public class MatchedRegistPostRes {

    // 매칭이 성사되었다는 알림을 받을 강사
    User user;

    // 알림 내용, Response로 들어갈 Web RTC 세션 Id
    String sessionId;
}
