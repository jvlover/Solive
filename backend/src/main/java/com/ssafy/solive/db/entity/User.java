package com.ssafy.solive.db.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Entity
public class User extends BaseEntity {
    private String loginId;
    private String loginPassword;
    private String name;
    private String email;
    private String pictureUrl;
    private String pictureName;
    private String introduce;
    private Long experience;
    private LocalDateTime signinTime;
    private int gender;
    private boolean isDeleted;
}
