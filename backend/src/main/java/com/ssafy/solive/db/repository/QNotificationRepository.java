package com.ssafy.solive.db.repository;

import com.ssafy.solive.api.matching.response.NotificationFindRes;
import java.util.List;

/**
 * Querydsl을 위한 Notification Repository interface
 */
public interface QNotificationRepository {

    List<NotificationFindRes> findNotification(Long userId, Integer pageNum);
}
