package com.ssafy.solive.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@DynamicInsert
@Entity
public class Student extends User {

    // 지금까지 질문한 문제 수
    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer questionCount;

    // 환전 가능한 SP
    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer solvePoint;

    /**
     * 학생이 Solve Point를 충전할 때
     *
     * @param solvePoint 충전할 금액
     */
    public void chargeSolvePoint(int solvePoint) {
        this.solvePoint += solvePoint;
    }
}
