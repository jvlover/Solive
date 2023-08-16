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
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@SuperBuilder
@DynamicInsert
@DiscriminatorValue("Teacher")
@Entity
public class Teacher extends User {

    // masterCode id, FK, 좋아하는 과목
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

    /**
     * 학생이 강사를 평가했을 때
     *
     * @param rating 추가할 평점
     */
    public void addRating(Integer rating) {
        this.ratingCount++;
        this.ratingSum += rating;
    }

    /**
     * 매칭 종료 후 강사의 푼 문제 수 증가
     */
    public void addSolvedCount() {
        this.solvedCount++;
    }
}
