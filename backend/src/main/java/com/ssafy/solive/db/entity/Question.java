package com.ssafy.solive.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Where;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
@Where(clause = "deleted_at is null")
@Entity
public class Question extends BaseEntity {

    // TODO: 관련 엔티티 구현하면 차후 수정 필요, FK
    private Integer studentId;

    // TODO: 관련 엔티티 구현하면 차후 수정 필요, FK
    private Integer masterCodeId;

    @Column(columnDefinition = "VARCHAR(255)")
    private String description;

    @Column(nullable = false, columnDefinition = "DATETIME DEFAULT NOW()")
    private LocalDateTime time;

    @Column(columnDefinition = "DATETIME DEFAULT NOW()")
    private LocalDateTime lastUpdateTime;

    @Column(nullable = false, columnDefinition = "BIT DEFAULT 0")
    private Boolean isMatched;

    @Column
    private LocalDateTime deletedAt;

    public void deleteQuestion() {
        this.deletedAt = LocalDateTime.now();
    }

    public void modifyQuestion(Question question) {
        this.masterCodeId = question.getMasterCodeId();
        this.description = question.getDescription();
        this.lastUpdateTime = LocalDateTime.now();
    }
}
