package com.ssafy.solive.db.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * SSE 연결을 관리하는 SseEmitter 객체와 이벤트 캐시를 맵 형태로 저장하고 관리하기 위한 Repository
 */
@Slf4j
@Repository
public class EmitterRepositoryImpl implements EmitterRepository {

    // ConcurrentHashMap : thread-safe -> 동시성 관리

    // 유저 별 SseEmitter 객체 저장하기 위한 맵. 유저가 subscribe 요청하면 맵에 저장됨
    // Key : emitter ID, Value : SseEmitter 객체
    // 알림 전송하기 위해 사용
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    // 이벤트 캐시를 저장하는 역할
    // Key : emitter ID, Value : SseEmitter 객체
    // 유저에게 전송되지 못한 이벤트를 캐시로 저장
    private final Map<String, Object> eventCache = new ConcurrentHashMap<>();

    // emitter 맵에 저장
    @Override
    public SseEmitter saveEmitter(String emitterId, SseEmitter sseEmitter) {
        emitters.put(emitterId, sseEmitter);
        return sseEmitter;
    }

    // eventCache 맵에 저장
    @Override
    public void saveEventCache(String eventCacheId, Object event) {
        eventCache.put(eventCacheId, event);
    }

    // 해당 유저와 관련된 모든 Emitter 찾기
    @Override
    public Map<String, SseEmitter> findAllEmitter(String userId) {
        return emitters.entrySet().stream()
            .filter(entry -> entry.getKey().startsWith(userId))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    // 해당 유저와 관련된 모든 EventCache 찾기
    @Override
    public Map<String, Object> findAllEventCache(String userId) {
        return eventCache.entrySet().stream()
            .filter(entry -> entry.getKey().startsWith(userId))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    // 해당 emitter 삭제
    @Override
    public void delete(String emitterId) {
        emitters.remove(emitterId);
    }

    // 해당 유저와 관련된 모든 emitter 삭제
    @Override
    public void deleteAllEmitter(String userId) {
        emitters.forEach((key, emitter) -> {
            if (key.startsWith(userId)) {
                emitters.remove(key);
            }
        });
    }

    // 해당 유저와 관련된 모든 EventCache 삭제
    @Override
    public void deleteAllEventCache(String userId) {
        eventCache.forEach((key, emitter) -> {
            if (key.startsWith(userId)) {
                eventCache.remove(key);
            }
        });
    }
}
