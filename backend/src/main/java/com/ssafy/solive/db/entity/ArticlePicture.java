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
public class ArticlePicture extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String fileName; // 실제 업로드 된 파일명

    @Column(nullable = false, columnDefinition = "VARCHAR(100)")
    private String originalName; // 원본 파일 이름

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String path; // 파일 업로드 경로

    @Column(nullable = false, columnDefinition = "VARCHAR(100)")
    private String contentType; // 확장자명

    @CreationTimestamp
    private LocalDateTime time; // 업로드 시간

    @Column
    private LocalDateTime deletedAt;

    public void deleteArticlePicture() {
        this.deletedAt = LocalDateTime.now();
    }
}
