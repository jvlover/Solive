package com.ssafy.solive.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

import lombok.AccessLevel;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
@DynamicInsert
@Where(clause = "deleted_at is null")
@Entity
public class Article extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "master_code_id")
    private MasterCode masterCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, columnDefinition = "VARCHAR(40)")
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false, columnDefinition = "BIGINT DEFAULT 0")
    private Long viewCount;

    @Column(nullable = false, columnDefinition = "BIGINT DEFAULT 0")
    private Long likeCount;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer reportCount;

    @Column
    private LocalDateTime deletedAt;

    @CreationTimestamp
    private LocalDateTime time;

    @CreationTimestamp
    private LocalDateTime lastUpdateTime;

    public void modifyArticle(MasterCode masterCode, String title, String content) {
        this.masterCode = masterCode;
        this.title = title;
        this.content = content;
        this.lastUpdateTime = LocalDateTime.now();
    }

    public void deleteArticle() {
        this.deletedAt = LocalDateTime.now();
    }

    public void likeArticle() {
        this.likeCount++;
    }

    public void reportArticle() {
        this.reportCount++;
    }

    public void incrementViewCount() {
        this.viewCount++;
    }
}
