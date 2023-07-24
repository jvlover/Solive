package com.ssafy.solive.db.entity;

import jakarta.persistence.Entity;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@Entity
public class User extends BaseEntity {

    private String loginId;
    private String loginPassword;
    private String nickname;
    private String email;
    private String pictureUrl;
    private String pictureName;
    private String introduce;
    private Long experience;
    private LocalDateTime signinTime;
    private int gender;
    private boolean isDeleted;

    public void init() {
        this.isDeleted = false;
        this.signinTime = LocalDateTime.now();
        this.experience = 0L;
    }
}
