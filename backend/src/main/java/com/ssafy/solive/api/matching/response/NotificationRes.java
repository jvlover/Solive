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

    // 알림 내용
    String content;
    
}
