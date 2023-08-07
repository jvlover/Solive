package com.ssafy.solive.db.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
@DynamicInsert
@IdClass(FavoriteId.class)
@Entity
public class Favorite implements Serializable {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student studentId;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private Teacher teacherId;

    // 즐겨찾기 한 시간
    @Column(nullable = false, columnDefinition = "DATETIME DEFAULT NOW()")
    LocalDateTime time;

    // 즐겨찾기 취소 시간
    LocalDateTime delatedAt;

    /**
     * 이미 즐겨찾기 정보가 DB에 있지만 즐겨찾기 삭제로 인해 다시 등록하는 경우 time 만 현재시간으로 갱신
     */
    public void addTime() {
        this.time = LocalDateTime.now();
        this.delatedAt = null; // 삭제 비활성
    }

    /**
     * 삭제 처리를 의미하기 위해 삭제 시간을 현재시간으로 등록
     */
    public void addDeleteAt() {
        this.delatedAt = LocalDateTime.now();
    }
}
