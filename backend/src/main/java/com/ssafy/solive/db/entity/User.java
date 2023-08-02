package com.ssafy.solive.db.entity;

import com.ssafy.solive.api.request.UserModifyPutReq;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
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
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@DynamicInsert
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@Entity
public class User extends BaseEntity {

    @Column(nullable = false, columnDefinition = "VARCHAR(40)")
    private String loginId;

    @Column(nullable = false, columnDefinition = "CHAR(60)")
    private String loginPassword;

    @Column(columnDefinition = "VARCHAR(255)")
    private String refreshToken;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer masterCodeId;

    @Column(nullable = false, columnDefinition = "VARCHAR(10)")
    private String nickname;

    @Column(nullable = false, columnDefinition = "VARCHAR(40)")
    private String email;

    @Column(columnDefinition = "VARCHAR(255)")
    private String pictureUrl;

    @Column(columnDefinition = "VARCHAR(100)")
    private String pictureName;

    @Column(columnDefinition = "VARCHAR(100)")
    private String fileName;

    @Column(columnDefinition = "VARCHAR(100)")
    private String pathName;

    @Column(columnDefinition = "VARCHAR(10)")
    private String contentType;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String introduce;

    @Column(columnDefinition = "BIGINT DEFAULT 0")
    private Long experience;

    @Column(columnDefinition = "DATETIME DEFAULT NOW()")
    private LocalDateTime signinTime;

    @Column(nullable = false)
    private Integer gender;

    private LocalDateTime deletedAt;

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void addDeleteAt() {
        this.deletedAt = LocalDateTime.now();
    }

    public void modifyUser(UserModifyPutReq userInfo) {
        this.nickname = userInfo.getNickname();
        this.email = userInfo.getEmail();
        this.pictureUrl = userInfo.getPictureUrl();
        this.pictureName = userInfo.getPictureName();
        this.fileName = userInfo.getFileName();
        this.pathName = userInfo.getPathName();
        this.contentType = userInfo.getContentType();
        this.introduce = userInfo.getIntroduce();
        this.gender = userInfo.getGender();
    }
}
