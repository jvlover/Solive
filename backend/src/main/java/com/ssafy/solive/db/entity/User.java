package com.ssafy.solive.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Entity
public class User extends BaseEntity {

    @Column(nullable = false)
    private String loginId;

    @Column(nullable = false)
    private String loginPassword;

    @Column(columnDefinition = "")
    private String refreshToken;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer masterCodeId;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String email;

    private String pictureUrl;

    private String pictureName;

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
