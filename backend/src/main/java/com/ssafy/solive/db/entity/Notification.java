package com.ssafy.solive.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Where;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Where(clause = "deleted_at is null")
@DynamicInsert
@Entity
public class Notification extends BaseEntity {

    // 알림을 받을 유저 id, FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receive_user_id")
    private User user;

    // 지원 신청 시간
    @Column(nullable = false, columnDefinition = "DATETIME DEFAULT NOW()")
    private LocalDateTime time;

    // 알림 제목
    @Column(nullable = false, columnDefinition = "VARCHAR(40)")
    private String title;

    // 알림 내용
    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String content;

    // 알림 읽었으면 읽은 시간, 아니면 null
    @Column
    private LocalDateTime readAt;

    // 알림이 삭제 되었으면 삭제된 시간, 아니면 null
    @Column
    private LocalDateTime deletedAt;

    /**
     * 알림 읽음 처리 시 readAt을 현재 시간으로 설정하기 위한 Method
     */
    public void modifyReadAt() {
        this.readAt = LocalDateTime.now();
    }

    /**
     * 알림 삭제 시 deletedAt을 현재 시간으로 설정하기 위한 method
     */
    public void deleteNotification() {
        this.deletedAt = LocalDateTime.now();
    }
}
