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

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
@Entity
public class QuestionPicture extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "questino_id")
    private Question question;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String url;

    @Column(nullable = false, columnDefinition = "VARCHAR(100)")
    private String pathName;

    @Column(nullable = false, columnDefinition = "VARCHAR(100)")
    private String fileName;

    @Column(nullable = false, columnDefinition = "VARCHAR(100)")
    private String imageName;

    @Column(nullable = false, columnDefinition = "VARCHAR(100)")
    private String contentType;

    @Column(nullable = false, columnDefinition = "DATETIME DEFAULT NOW()")
    private LocalDateTime time;

    @Column(nullable = false)
    private Integer size;
}
