package com.ssafy.solive.api.matching.response;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * 알림 조회 Get 요청 시 결과 Response DTO
 */
@Data
public class NotificationFindRes {

    // 알림 Id
    Long id;

    // 알림 제목
    String title;

    // 알림 내용
    String content;

    // 알림 생성 시간
    String time;

    // 알림 읽었는지 여부
    String readAt;

    public NotificationFindRes() {
    }

    // Querydsl을 위한 생성자
    public NotificationFindRes(Long id, String title, String content, LocalDateTime time,
        LocalDateTime readAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.time = time.toString();
        if (readAt == null) {
            this.readAt = "unread";
        } else {
            this.readAt = "read";
        }
    }
}
