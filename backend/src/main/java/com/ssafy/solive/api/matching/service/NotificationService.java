package com.ssafy.solive.api.matching.service;

import com.ssafy.solive.db.entity.User;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationService {

    SseEmitter subscribe(Long userId, String lastEventId);

    String includeTimeToEmitterId(String userId);

    void sendNotification(SseEmitter sseEmitter, String eventId, String emitterId, Object data);

    boolean hasLostData(String lastEventId);

    void sendLostData(String lastEventId, String userId, String emitterId, SseEmitter sseEmitter);

    void send(User user, String title, String content);
}
