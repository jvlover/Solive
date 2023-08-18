package com.ssafy.solive.api.matching.request;

import lombok.Data;

/**
 * 알림 삭제 API에 대한 Request
 */
@Data
public class NotificationDeletePutReq {

    // 삭제할 알림 Id
    Long notificationId;

    // 삭제 시도한 유저 Id
    Long userId;
}
