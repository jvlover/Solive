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
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Where;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@DynamicInsert
@Where(clause = "deleted_at is null")
@Entity
public class Apply extends BaseEntity {

    // question의 id, FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    // user(teacher)의 id, FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    // 지원 신청 시간
    @CreationTimestamp
    private LocalDateTime time;

    // 강사가 문제를 푸는 데 예상 시간으로 작성한 내용
    @Column(nullable = false, columnDefinition = "INT")
    private Integer estimatedTime;

    // 강사가 문제를 풀어 주는 데에 대한 비용으로 제시한 SP
    @Column(nullable = false, columnDefinition = "INT")
    private Integer solvePoint;

    // 지원이 취소가 되었으면 삭제된 시간, 아니면 null
    @Column
    private LocalDateTime deletedAt;

    /*
     * 지원 취소 시 deletedAt을 현재 시간으로 설정하기 위한 method
     */
    public void deleteApply() {
        this.deletedAt = LocalDateTime.now();
    }
}
