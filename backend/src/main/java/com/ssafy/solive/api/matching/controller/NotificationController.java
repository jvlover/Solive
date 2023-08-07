package com.ssafy.solive.api.matching.controller;

import com.ssafy.solive.api.matching.service.NotificationService;
import com.ssafy.solive.api.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/*
 *  매칭 상황에서 유저가 서버로부터 알림을 받기 위한 API를 모은 컨트롤러
 */

@Slf4j
@RestController
@RequestMapping("/notification")
@CrossOrigin("*")
public class NotificationController {

    private static final String SUCCESS = "success";  // API 성공 시 return

    private final UserService userService;
    private final NotificationService notificationService;

    @Autowired
    public NotificationController(UserService userService,
        NotificationService notificationService) {
        this.userService = userService;
        this.notificationService = notificationService;
    }

    /**
     * 클라이언트가 알림을 구독하기 위한 기능
     *
     * @param request : userID를 access-token에서 가져와야 함
     * @return sseEmitter : Emitter
     */
    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(HttpServletRequest request,
        @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {

        log.info("NotificationController_subscribe_start: " + request.toString());

        String accessToken = request.getHeader("access-token");
        Long userId = userService.getUserIdByAccessToken(accessToken);

        SseEmitter sseEmitter = notificationService.subscribe(userId, lastEventId);

        log.info("NotificationController_subscribe_end: " + userId.toString() + ", "
            + lastEventId);

        return sseEmitter;
    }
}
