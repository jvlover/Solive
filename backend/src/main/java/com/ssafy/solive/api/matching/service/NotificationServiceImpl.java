package com.ssafy.solive.api.matching.service;

import com.ssafy.solive.api.matching.response.NotificationRes;
import com.ssafy.solive.db.entity.Notification;
import com.ssafy.solive.db.entity.User;
import com.ssafy.solive.db.repository.EmitterRepository;
import com.ssafy.solive.db.repository.NotificationRepository;
import java.io.IOException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/*
 *  매칭 과정 중 알림 API 서비스
 */

@Slf4j
@Transactional
@Service
public class NotificationServiceImpl implements NotificationService {

    // SSE 연결 지속 시간 설정
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    private final EmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationServiceImpl(EmitterRepository emitterRepository,
        NotificationRepository notificationRepository) {
        this.emitterRepository = emitterRepository;
        this.notificationRepository = notificationRepository;
    }

    /**
     * 클라이언트가 알림을 구독하는 API 서비스
     *
     * @param userId : 유저 PK
     * @return sseEmitter 객체
     */
    @Override
    public SseEmitter subscribe(Long userId, String lastEventId) {
        // emitterId 생성
        String emitterId = includeTimeToEmitterId(Long.toString(userId));
        // SseEmitter 객체 생성
        SseEmitter sseEmitter = emitterRepository.saveEmitter(emitterId,
            new SseEmitter(DEFAULT_TIMEOUT));

        // emitter가 완료되면 emitter 삭제
        sseEmitter.onCompletion(() -> emitterRepository.delete(emitterId));
        // emitter가 타임아웃되면 emitter 삭제
        sseEmitter.onTimeout(() -> emitterRepository.delete(emitterId));

        // 연결 생성 초기에 503 에러 방지하기 위한 더미 이벤트 데이터를 클라이언트에 전송
        String eventID = includeTimeToEmitterId(Long.toString(userId));
        sendNotification(sseEmitter, eventID, emitterId, "EventStream Created.");

        // 클라이언트가 수신하지 않은 event 존재할 경우 전송해서 event 유실 방지
        if (hasLostData(lastEventId)) {
            sendLostData(lastEventId, Long.toString(userId), emitterId, sseEmitter);
        }

        // 생성된 SseEmitter 객체 클라이언트에게 전달. 이를 통해서 서버로부터 알림 수신 및 처리
        return sseEmitter;
    }

    /**
     * SseEmitter를 식별하는 emitterId를 생성하기 위한 함수. userId에 emitter 생성 시간 붙여서 생성 시간 붙이는 이유 : 데이터가 유실 될 경우
     * 그 시점을 파악하기 용이, 데이터가 언제 보내졌는지도 알 수 있다
     *
     * @param userId : 유저 PK를 스트링으로 변환
     * @return emitterID : SseEmitter 식별
     */
    @Override
    public String includeTimeToEmitterId(String userId) {
        return userId + "_" + System.currentTimeMillis();
    }

    /**
     * SseEmitter 객체를 사용하여 SSE를 클라이언트에게 전송
     *
     * @param sseEmitter SseEmitter 객체
     * @param eventId    eventId
     * @param emitterId  emitter의 id
     * @param data       data
     */
    @Override
    public void sendNotification(SseEmitter sseEmitter, String eventId, String emitterId,
        Object data) {
        try {
            sseEmitter.send(SseEmitter.event()
                .id(eventId)
                .name("sse")
                .data(data));
        } catch (IOException ioException) {
            emitterRepository.delete(emitterId);
        }
    }

    /**
     * lastEventId가 비어 있지 않은지 확인하여 클라이언트가 이전 이벤트 이후에 새로운 이벤트를 놓치지 않았는지 확인
     *
     * @param lastEventId : 마지막 이벤트 ID
     * @return True : 이벤트 손실 없음, False : 이벤트 손실 있음
     */
    @Override
    public boolean hasLostData(String lastEventId) {
        return !lastEventId.isEmpty();
    }

    /**
     * 미수신한 데이터 전송
     *
     * @param lastEventId
     * @param userId
     * @param emitterId
     * @param sseEmitter
     */
    @Override
    public void sendLostData(String lastEventId, String userId, String emitterId,
        SseEmitter sseEmitter) {
        Map<String, Object> eventCaches = emitterRepository.findAllEventCache(userId);
        eventCaches.entrySet().stream()
            .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
            .forEach(
                entry -> sendNotification(sseEmitter, entry.getKey(), emitterId, entry.getValue()));
    }

    /**
     * 알림을 생성한 후 지정된 클라이언트에게 알림 전송
     *
     * @param user    알림을 받을 유저
     * @param title   알림 제목
     * @param content 알림 내용
     */
    @Override
    public void send(User user, String title, String content) {
        Notification notification = notificationRepository.save(Notification.builder()
            .user(user)
            .title(title)
            .content(content)
            .build());

        String userId = Long.toString(user.getId());
        String eventId = userId + "_" + System.currentTimeMillis();
        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitter(userId);
        emitters.forEach(
            (key, emitter) -> {
                emitterRepository.saveEventCache(key, notification);
                sendNotification(emitter, eventId, key,
                    NotificationRes.builder()
                        .title(title)
                        .content(content)
                        .build());
            }
        );
    }
}
