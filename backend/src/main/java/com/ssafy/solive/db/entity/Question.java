package com.ssafy.solive.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "master_code_id")
    private MasterCode masterCode;

    @Column(nullable = false, columnDefinition = "VARCHAR(40)")
    private String title;

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

    public void modifyQuestion(MasterCode masterCode, String title, String description) {
        this.masterCode = masterCode;
        this.title = title;
        this.description = description;
        this.lastUpdateTime = LocalDateTime.now();
    }
}
