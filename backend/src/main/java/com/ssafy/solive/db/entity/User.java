package com.ssafy.solive.db.entity;

import com.ssafy.solive.api.request.UserModifyProfilePutReq;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
@DynamicInsert
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@Entity
public class User extends BaseEntity {

    // 유저 타입 : 학생(1), 선생님(2)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "master_code_id")
    private MasterCode masterCodeId;

    // 유저 상태 번호 : 로그아웃(11), 로그인(12), 강의중(13)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "state_id")
    private MasterCode stateId;

    // 로그인 아이디
    @Column(nullable = false, columnDefinition = "VARCHAR(40)")
    private String loginId;

    // 로그인 후 암호화 된 비밀번호
    @Column(nullable = false, columnDefinition = "CHAR(60)")
    private String loginPassword;

    // 서버가 가지고 있는 클라이언트의 refreshToken
    @Column(columnDefinition = "VARCHAR(255)")
    private String refreshToken;

    // 웹에서 사용할 닉네임
    @Column(nullable = false, columnDefinition = "VARCHAR(10)")
    private String nickname;

    // 이메일
    @Column(nullable = false, columnDefinition = "VARCHAR(40)")
    private String email;

    // 프로필 사진의 URL
    @Column(columnDefinition = "VARCHAR(255)")
    private String pictureUrl;

    // 프로필 사진의 이름
    @Column(columnDefinition = "VARCHAR(100)")
    private String pictureName;

    // 프로필 사진의 실제 업로드 된 파일이름
    @Column(columnDefinition = "VARCHAR(100)")
    private String fileName;

    // 프로필 사진의 업로드 경로
    @Column(columnDefinition = "VARCHAR(100)")
    private String pathName;

    // 프로필 사진의 확장자명
    @Column(columnDefinition = "VARCHAR(10)")
    private String contentType;

    // 소개글
    @Column(columnDefinition = "VARCHAR(255)")
    private String introduce;

    // 경험치
    @Column(columnDefinition = "BIGINT DEFAULT 0")
    private Long experience;

    // 회원가입 시간
    @Column(columnDefinition = "DATETIME DEFAULT NOW()")
    private LocalDateTime signinTime;

    // 성별
    @Column(nullable = false)
    private Integer gender;

    /**
     * 로그인 시 사용자의 state를 로그인 상태(12)로 변경
     *
     * @param masterCode 로그인 상태(12)
     */
    public void setLoginState(MasterCode masterCode) {
        this.stateId = masterCode;
    }

    // 회원탈퇴 여부, 회원탈퇴시 탈퇴시간 부여
    private LocalDateTime deletedAt;

    /**
     * @param userInfo userInfo
     */
    public void modifyUserProfile(UserModifyProfilePutReq userInfo) {
        this.pictureUrl = userInfo.getPictureUrl();
        this.pictureName = userInfo.getPictureName();
        this.fileName = userInfo.getFileName();
        this.pathName = userInfo.getPathName();
        this.contentType = userInfo.getContentType();
        this.nickname = userInfo.getNickname();
        this.gender = userInfo.getGender();
        this.introduce = userInfo.getIntroduce();
    }

    /**
     * 비밀번호 수정
     *
     * @param newPassword newPassword
     */
    public void modifyUserPassword(String newPassword) {
        this.loginPassword = newPassword;
    }

    /**
     * refreshToken 갱신
     *
     * @param refreshToken refreshToken
     */
    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    /**
     * 회원탈퇴 -> 탈퇴일시 추가
     */
    public void addDeleteAt() {
        this.deletedAt = LocalDateTime.now();
    }

    /**
     * 임시라 지워질 듯
     *
     * @param code 바뀔 코드
     */
    public void setCode(MasterCode code) {
        this.masterCodeId = code;
    }
}
