package com.ssafy.solive.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@DynamicInsert
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
public class User extends BaseEntity {

    @Column(nullable = false, columnDefinition = "VARCHAR(40)")
    private String loginId;

    @Column(nullable = false, columnDefinition = "CHAR(60)")
    private String loginPassword;

    @Column(nullable = false, columnDefinition = "VARCHAR(40)")
    private String refreshToken;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer masterCodeId;

    @Column(nullable = false, columnDefinition = "VARCHAR(10)")
    private String nickname;

    @Column(nullable = false, columnDefinition = "VARCHAR(40)")
    private String email;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String pictureUrl;

    @Column(nullable = false, columnDefinition = "VARCHAR(100)")
    private String pictureName;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String introduce;

    @Column(nullable = false)
    private Integer gender;

    @Column(columnDefinition = "BIGINT DEFAULT 0")
    private Long experience;

    @Column(columnDefinition = "DATETIME DEFAULT NOW()")
    private LocalDateTime signinTime;

    @Column(columnDefinition = "BIT DEFAULT 0")
    private Boolean isDeleted;

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}
