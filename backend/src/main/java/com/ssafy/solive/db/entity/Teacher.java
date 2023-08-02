package com.ssafy.solive.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@DiscriminatorValue("Teacher")
@Entity
public class Teacher extends User {

    // masterCode id, FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private MasterCode masterCode;

    // 지금까지 푼 문제 수
    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer solvedCount;

    // 별점 합
    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer ratingSum;

    // 별점 수
    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer ratingCount;

    // 환전 가능한 SP
    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer solvePoint;
}
