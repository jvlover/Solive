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
public class Question extends BaseEntity {

    // user(student)의 id, FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    // masterCode id, FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "master_code_id")
    private MasterCode masterCode;

    // 문제 제목
    @Column(nullable = false, columnDefinition = "VARCHAR(40)")
    private String title;

    // 문제 설명
    @Column(columnDefinition = "VARCHAR(255)")
    private String description;

    // 문제 생성 시간
    @CreationTimestamp
    private LocalDateTime time;

    // 문제 마지막 수정 시간
    @CreationTimestamp
    private LocalDateTime lastUpdateTime;

    // 문제가 매칭 되었으면 2, 강사들의 지원이 있는 상태면 1, 문제가 등록만 된 초기 상태면 0
    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer matchingState;

    // 문제가 삭제 되었으면 삭제된 시간, 아니면 null
    @Column
    private LocalDateTime deletedAt;

    /**
     * 문제 삭제 시 deletedAt을 현재 시간으로 설정하기 위한 method
     */
    public void deleteQuestion() {
        this.deletedAt = LocalDateTime.now();
    }

    /**
     * 문제 수정 시 수정 사항 설정 method
     *
     * @param masterCode  : 문제 분류 수정
     * @param title       : 문제 제목 수정
     * @param description : 문제 설명 수정
     */
    public void modifyQuestion(MasterCode masterCode, String title, String description) {
        this.masterCode = masterCode;
        this.title = title;
        this.description = description;
        // 마지막 수정 시간을 현재 시간으로 설정
        this.lastUpdateTime = LocalDateTime.now();
    }

    /**
     * 문제의 매칭 상태 변경 method
     *
     * @param state : 상태 수정 내용
     */
    public void modifyMatchingState(int state) {
        this.matchingState = state;
    }
}
