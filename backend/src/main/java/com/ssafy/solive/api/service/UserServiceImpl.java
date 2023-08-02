package com.ssafy.solive.api.service;

import com.ssafy.solive.api.request.TeacherRatePostReq;
import com.ssafy.solive.api.request.UserLoginPostReq;
import com.ssafy.solive.api.request.UserModifyPasswordPutReq;
import com.ssafy.solive.api.request.UserModifyProfilePutReq;
import com.ssafy.solive.api.request.UserRegistPostReq;
import com.ssafy.solive.api.response.UserLoginPostRes;
import com.ssafy.solive.api.response.UserPrivacyPostRes;
import com.ssafy.solive.api.response.UserProfilePostRes;
import com.ssafy.solive.common.exception.PasswordMismatchException;
import com.ssafy.solive.config.JwtConfiguration;
import com.ssafy.solive.db.entity.MasterCode;
import com.ssafy.solive.db.entity.Student;
import com.ssafy.solive.db.entity.Teacher;
import com.ssafy.solive.db.entity.User;
import com.ssafy.solive.db.repository.MasterCodeRepository;
import com.ssafy.solive.db.repository.StudentRepository;
import com.ssafy.solive.db.repository.TeacherRepository;
import com.ssafy.solive.db.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Transactional
@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private StudentRepository studentRepository;
    private TeacherRepository teacherRepository;
    private MasterCodeRepository masterCodeRepository;
    private JwtConfiguration jwtConfiguration;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, StudentRepository studentRepository,
        TeacherRepository teacherRepository, MasterCodeRepository masterCodeRepository,
        JwtConfiguration jwtConfiguration) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.masterCodeRepository = masterCodeRepository;
        this.jwtConfiguration = jwtConfiguration;
    }

    /**
     * @param registInfo
     * @return
     */
    @Override
    public User registUser(UserRegistPostReq registInfo) {
        log.info("UserService_registUser_start: " + registInfo.toString());
        // 비밀번호에 Bcrypt 적용
        String hashedPassword = BCrypt.hashpw(registInfo.getLoginPassword(), BCrypt.gensalt());
        // 마스터 코드 객체 생성
        MasterCode masterCode = masterCodeRepository.findById(registInfo.getMasterCodeId()).get();

        // 학생으로 회원가입 요청한 경우
        if (registInfo.getMasterCodeId() == 2) {
            Student student = Student.builder()
                .loginId(registInfo.getLoginId())
                .loginPassword(hashedPassword)
                .masterCodeId(masterCode)
                .nickname(registInfo.getNickname())
                .email(registInfo.getEmail())
                .gender(registInfo.getGender())
                .build();

            log.info("UserService_registUser_end: " + student.toString());

            try {
                studentRepository.save(student);
                log.info("UserService_registUser_end: " + student.toString());
                return student;
            } catch (Exception e) {
                // TODO: 예외 관련 세분화 처리 ex) 중복확인, 미가입 유저 등
                e.printStackTrace();
                log.info("UserService_registUser_end: null");
                return null;
            }
        } else {  // 임시로 나머지 경우 다 강사가 회원가입 요청한 경우로 처리함
            Teacher teacher = Teacher.builder()
                .loginId(registInfo.getLoginId())
                .loginPassword(hashedPassword)
                .masterCodeId(masterCode)
                .nickname(registInfo.getNickname())
                .email(registInfo.getEmail())
                .gender(registInfo.getGender())
                .build();

            log.info("UserService_registUser_end: " + teacher.toString());

            try {
                teacherRepository.save(teacher);
                log.info("UserService_registUser_end: " + teacher.toString());
                return teacher;
            } catch (Exception e) {
                // TODO: 예외 관련 세분화 처리 ex) 중복확인, 미가입 유저 등
                e.printStackTrace();
                log.info("UserService_registUser_end: null");
                return null;
            }
        }

    }

    /**
     * 입력한 패스워드가 맞는지 확인 후 Refresh Token을 DB에 저장하고 Access Token을 반환
     *
     * @param loginInfo: 로그인 한 Id, Password
     * @return UserLoginPostRes: accessToken, refreshToken 값
     */
    @Override
    public UserLoginPostRes loginAndGetTokens(UserLoginPostReq loginInfo) {
        log.info("UserService_loginAndGetAccessToken_start: " + loginInfo.toString());
        String userLoginId = loginInfo.getLoginId();
        String userLoginPassword = loginInfo.getLoginPassword();
        User user = userRepository.findByLoginId(userLoginId);

        // 로그인 성공
        if (BCrypt.checkpw(userLoginPassword, user.getLoginPassword())) {
            Long userId = userRepository.findByLoginId(userLoginId).getId();
            String accessToken = jwtConfiguration.createAccessToken("userid", userId);
            String refreshToken = jwtConfiguration.createRefreshToken("userid", userId);

            // RefreshToken을 user DB에 저장
            user.updateRefreshToken(refreshToken);

            log.info("UserService_loginAndGetAccessToken_end\naccessToken: " + accessToken
                + "\nrefreshToken: " + refreshToken);

            // Token들을 return
            return UserLoginPostRes.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .loginId(userLoginId)
                .masterCodeName(
                    masterCodeRepository.findById(user.getMasterCodeId().getId()).get().getName())
                .nickname(user.getNickname())
                .build();
        } else { // 로그인 실패
            throw new PasswordMismatchException();
        }
    }

    /**
     * accessTokendmfh userId를 조회
     *
     * @param accessToken
     * @return userId
     */
    @Override
    public Long getUserIdByAccessToken(String accessToken) {
        try {
            Long userId = jwtConfiguration.getUserId(accessToken);
            log.info("UserService_getUserIdByAccessToken_end: " + userId);
            return userId;
        } catch (UnsupportedEncodingException e) {
            // TODO: Exception처리
            throw new RuntimeException(e);
        }
    }

    /**
     * userId로 프로필 정보 조회
     *
     * @param userId
     * @return UserProfilePostRes
     */
    @Override
    public UserProfilePostRes getUserProfileByUserId(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return UserProfilePostRes.builder()
                .pictureUrl(user.getPictureUrl())
                .pictureName(user.getPictureName())
                .fileName(user.getFileName())
                .pathName(user.getFileName())
                .contentType(user.getContentType())
                .nickname(user.getNickname())
                .gender(user.getGender())
                .experience(user.getExperience())
                .introduce(user.getIntroduce())
                .build();
        } else {
            // TODO: Exception
            return null;
        }
    }

    /**
     * userId로 개인정보 조회
     *
     * @param userId
     * @return UserPrivacyPostRes
     */
    @Override
    public UserPrivacyPostRes getUserPrivacyByUserId(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return UserPrivacyPostRes.builder()
                .email(user.getEmail())
                .signinTime(user.getSigninTime())
                .build();
        } else {
            // TODO: Exception
            return null;
        }
    }

    /**
     * 유저 프로필 수정
     *
     * @param userId
     * @param userInfo 바꿀 정보들
     */
    @Override
    public void modifyUserProfile(Long userId, UserModifyProfilePutReq userInfo) {
        log.info("UserService_modifyUserProfile_start: \nuserId: " + userId + "\nuserInfo: "
            + userInfo.toString());
        User user = userRepository.findById(userId).get();
        log.info("UserService_modifyUserProfile_mid: \nuser: " + user.toString());
        user.modifyUserProfile(userInfo);

        log.info("UserService_modifyUserProfile_mid: \nmodifiedUser: " + user.toString());

        userRepository.save(user);
        log.info("UserService_modifyUserProfiler_end");
    }

    /**
     * 비밀번호 변경
     *
     * @param userId
     * @param passwords 기존비밀번호, 새로운비밀번호
     */
    @Override
    public void modifyUserPassword(Long userId, UserModifyPasswordPutReq passwords) {
        log.info("UserService_modifyUserPrivacy_start: \nuserId: " + userId + "\npasswords: "
            + passwords.toString());
        User user = userRepository.findById(userId).get();
        String oldPassword = passwords.getOldPassword();
        String newPassword = passwords.getNewPassword();

        // 비밀번호 일치 확인
        if (BCrypt.checkpw(oldPassword, user.getLoginPassword())) {
            String hashedNewPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            user.modifyUserPassword(hashedNewPassword);
        } else { // 비밀번호 불일치

        }
        log.info("UserService_modifyUserPassword_end");
    }

    /**
     * 회원 탈퇴
     *
     * @param userId
     */
    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId).get();
        user.addDeleteAt();
    }

    /**
     * 임시함수여서 지울듯
     *
     * @param userId
     * @param code
     */
    @Override
    public void setCode(Long userId, Integer code) {
        User user = userRepository.findById(userId).get();
        MasterCode masterCode = masterCodeRepository.findById(code).get();
        user.setCode(masterCode);
    }

    /**
     * 학생의 Solve Point 충전
     *
     * @param userId
     * @param solvePoint 충전금액
     */
    @Override
    public void chargeSolvePoint(Long userId, Integer solvePoint) {
        Student student = studentRepository.findById(userId).get();
        student.chargeSolvePoint(solvePoint);
    }

    /**
     * 강사의 Solve Point 출금
     *
     * @param userId
     * @param solvePoint 출금금액
     */
    @Override
    public void cashOutSolvePoint(Long userId, Integer solvePoint) {
        Teacher teacher = teacherRepository.findById(userId).get();
        teacher.cashOutSolvePoint(solvePoint);
    }

    /**
     * 학생의 강사 평점 입력
     *
     * @param ratingInfo 입력된 평점
     */
    @Override
    public void rateTeacher(TeacherRatePostReq ratingInfo) {
        Teacher teacher = teacherRepository.findById(ratingInfo.getUserId()).get();
        teacher.addRating(ratingInfo.getRating());
    }
}
