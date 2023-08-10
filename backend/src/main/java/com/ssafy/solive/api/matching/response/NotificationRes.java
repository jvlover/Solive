package com.ssafy.solive.api.matching.response;

import lombok.Builder;
import lombok.Data;

/**
 * Notification 클라이언트에 전달하는 Response
 */
@Data
@Builder
public class NotificationRes {

    // 알림 제목
    String title;

    // 알림 내용. 풀이 지원 신청일 경우 문제 제목, 매칭 성사일 경우 매칭 세션 Id
    String content;

}
