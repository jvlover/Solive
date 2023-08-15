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
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Builder
@DynamicInsert
@Entity
public class QuestionPicture extends BaseEntity {

    // question의 id, FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    // 경로에 저장된 이미지 파일 이름
    @Column(nullable = false, columnDefinition = "VARCHAR(100)")
    private String fileName;

    // 유저가 제출한 이미지 오리지널 이름
    @Column(nullable = false, columnDefinition = "VARCHAR(100)")
    private String originalName;

    // 문제 이미지 절대 경로
    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String path;

    // 이미지 타입
    @Column(nullable = false, columnDefinition = "VARCHAR(100)")
    private String contentType;

    // 이미지 올린 시간
    @Column(nullable = false, columnDefinition = "DATETIME DEFAULT NOW()")
    private LocalDateTime time;
}
