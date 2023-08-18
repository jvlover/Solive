package com.ssafy.solive.db.repository;

import java.util.Map;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface EmitterRepository {

    SseEmitter saveEmitter(String emitterId, SseEmitter sseEmitter);

    void saveEventCache(String eventCacheId, Object event);

    Map<String, SseEmitter> findAllEmitter(String userId);

    Map<String, Object> findAllEventCache(String userId);

    void delete(String emitterId);

    void deleteAllEmitter(String userId);

    void deleteAllEventCache(String userId);
}
