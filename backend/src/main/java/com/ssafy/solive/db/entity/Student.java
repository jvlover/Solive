package com.ssafy.solive.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
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
@DiscriminatorValue("Student")
@Entity
public class Student extends User {

    // 지금까지 질문한 문제 수
    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer questionCount;

    /**
     * 학생이 질문한 문제 수 증가
     */
    public void incrementQuestionCount() {
        this.questionCount++;
    }
}
