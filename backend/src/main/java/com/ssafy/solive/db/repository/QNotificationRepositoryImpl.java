package com.ssafy.solive.db.repository;

import static com.ssafy.solive.db.entity.QNotification.notification;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.solive.api.matching.response.NotificationFindRes;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * Querydsl을 위한 Notification Repository 구현
 */
@Slf4j
@Repository
public class QNotificationRepositoryImpl implements QNotificationRepository {

    private final JPAQueryFactory queryFactory;

    public QNotificationRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 유저 Id로 알림 조회
     *
     * @param userId  : 유저 식별자
     * @param pageNum : 목록 페이지 넘버
     * @return : 검색 결과 NotificationFindRes List
     */
    @Override
    public List<NotificationFindRes> findNotification(Long userId, Integer pageNum) {

        log.info("QNotificationRepository_findNotification_start: " + userId.toString());

        return queryFactory
            .select(Projections.constructor(NotificationFindRes.class,
                notification.id.as("id"),
                notification.title.as("title"),
                notification.content.as("content"),
                notification.time.as("time"),
                notification.readAt.as("readAt")))
            .from(notification)
            .where(userIdEq(userId))
            .orderBy(notification.time.desc())
            .offset(pageNum * 8)
            .limit(8)
            .fetch();
    }

    /**
     * user id로 notification 조회하기 위한 where 절에서 사용
     */
    private BooleanExpression userIdEq(Long id) {
        return notification.user.id.eq(id);
    }
}
