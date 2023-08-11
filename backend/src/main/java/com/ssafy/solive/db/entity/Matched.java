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
import org.hibernate.annotations.DynamicInsert;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@DynamicInsert
@Entity
public class Matched extends BaseEntity {

    // 강사의 요청을 수락한 학생의 id, FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    // 풀이 지원 요청이 받아들여져서 매칭된 강사의 id, FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    // question의 id, FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    // 매칭 생성 시간
    @Column(nullable = false, columnDefinition = "DATETIME DEFAULT NOW()")
    private LocalDateTime matchedTime;

    // 실제 강의 시작 시간 (강의 준비 시간 = startTime - matchedTime)
    @Column(columnDefinition = "DATETIME")
    private LocalDateTime startTime;

    // 강의 종료 시간
    @Column(columnDefinition = "DATETIME")
    private LocalDateTime endTime;

    // 매칭에 들어간 Solve Point
    @Column(nullable = false, columnDefinition = "INT")
    private Integer solvePoint;

    // 매칭 중에 강의 시간 연장 횟수
    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer extensionCount;

    // 매칭 신고 여부
    @Column
    private LocalDateTime reportedAt;

    // 매칭 끝나고 영상 저장 시 비디오 url
    @Column(columnDefinition = "VARCHAR(255)")
    private String videoUrl;

    // 강의가 진행 될 Web RTC 세션 Id
    @Column(nullable = false, columnDefinition = "VARCHAR(100)")
    private String sessionId;

    /**
     * 매칭이 시작될 때 startTime 설정
     */
    public void modifyStartTime() {
        this.startTime = LocalDateTime.now();
    }

    /**
     * 연장 횟수 증가
     */
    public void addExtensionCount() {
        this.extensionCount++;
    }

    /**
     * 매칭이 끝날 때 endTime 설정
     */
    public void modifyEndTime() {
        this.endTime = LocalDateTime.now();
    }

    /**
     * 매칭이 끝날 때 강의 영상 Video Url 저장
     *
     * @param videoUrl : 강의 영상 Video Url
     */
    public void modifyVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
