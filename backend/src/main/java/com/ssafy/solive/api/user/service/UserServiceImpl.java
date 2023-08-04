package com.ssafy.solive.api.user.service;

import com.ssafy.solive.api.user.request.TeacherRatePostReq;
import com.ssafy.solive.api.user.request.UserLoginPostReq;
import com.ssafy.solive.api.user.request.UserModifyPasswordPutReq;
import com.ssafy.solive.api.user.request.UserModifyProfilePutReq;
import com.ssafy.solive.api.user.request.UserRegistPostReq;
import com.ssafy.solive.api.user.response.UserLoginPostRes;
import com.ssafy.solive.api.user.response.UserPrivacyPostRes;
import com.ssafy.solive.api.user.response.UserProfilePostRes;
import com.ssafy.solive.common.exception.ImageUploadFailException;
import com.ssafy.solive.common.exception.InvalidMasterCodeException;
import com.ssafy.solive.common.exception.user.PasswordMismatchException;
import com.ssafy.solive.common.exception.user.UserNotFoundException;
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
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Transactional
@Service
public class UserServiceImpl implements UserService {

    // 파일 업로드 경로
    private final String uploadFilePath = "C:/solive/image/";

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final MasterCodeRepository masterCodeRepository;
    private final JwtConfiguration jwtConfiguration;

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
     * @param registInfo 회원가입 정보
     * @return User
     */
    @Override
    public User registUser(UserRegistPostReq registInfo) {
        log.info("UserService_registUser_start: " + registInfo.toString());
        // 비밀번호에 Bcrypt 적용
        String hashedPassword = BCrypt.hashpw(registInfo.getLoginPassword(), BCrypt.gensalt());
        // 마스터 코드 객체 생성
        MasterCode masterCode = masterCodeRepository.findById(registInfo.getMasterCodeId())
            .orElseThrow(InvalidMasterCodeException::new);
        MasterCode logoutState = masterCodeRepository.findById(11)
            .orElseThrow(InvalidMasterCodeException::new); // 로그아웃 상태로 초기화

        // 학생으로 회원가입 요청한 경우
        if (registInfo.getMasterCodeId() == 1) {
            Student student = Student.builder()
                .masterCodeId(masterCode)
                .stateId(logoutState)
                .loginId(registInfo.getLoginId())
                .loginPassword(hashedPassword)
                .nickname(registInfo.getNickname())
                .email(registInfo.getEmail())
                .gender(registInfo.getGender())
                .build();

            log.info("UserService_registUser_mid: " + student.toString());

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
                .masterCodeId(masterCode)
                .stateId(logoutState)
                .loginId(registInfo.getLoginId())
                .loginPassword(hashedPassword)
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

            user.setLoginState(masterCodeRepository.findById(12)
                .orElseThrow(InvalidMasterCodeException::new)); // 로그인 상태로 변경
            // RefreshToken을 user DB에 저장
            user.updateRefreshToken(refreshToken);

            log.info("UserService_loginAndGetAccessToken_end\naccessToken: " + accessToken
                + "\nrefreshToken: " + refreshToken);

            // Token들을 return
            return UserLoginPostRes.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .masterCodeId(user.getMasterCodeId().getId())
                .nickname(user.getNickname())
                .build();
        } else { // 로그인 실패
            throw new PasswordMismatchException();
        }
    }

    @Override
    public void logout(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        MasterCode stateId = masterCodeRepository.findById(11)
            .orElseThrow(InvalidMasterCodeException::new);
        user.logout(stateId);
    }

    /**
     * accessToken으로 userId를 조회
     *
     * @param accessToken accessToken
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
     * @param userId userId
     * @return UserProfilePostRes
     */
    @Override
    public UserProfilePostRes getUserProfileByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
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
    }

    /**
     * userId로 개인정보 조회
     *
     * @param userId userId
     * @return UserPrivacyPostRes
     */
    @Override
    public UserPrivacyPostRes getUserPrivacyByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return UserPrivacyPostRes.builder()
            .email(user.getEmail())
            .signinTime(user.getSigninTime())
            .build();
    }

    /**
     * 유저 프로필 수정
     *
     * @param userId         userId
     * @param userInfo       바꿀 정보들
     * @param profilePicture 변경할 프로필 사진
     */
    @Override
    public void modifyUserProfile(Long userId, UserModifyProfilePutReq userInfo,
        MultipartFile profilePicture) {
        log.info("UserService_modifyUserProfile_start: \nuserId: " + userId + "\nuserInfo: "
            + userInfo.toString());
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        log.info(
            "UserService_modifyUserProfile_mid: \nuser: " + user.toString() + "\nprofilePicture: "
                + profilePicture);

        user.modifyUserProfile(userInfo);
        log.info("UserService_modifyUserProfile_mid: \nmodifiedUser: " + user.toString());

        // 파일 확장자 명
        String suffix = profilePicture.getOriginalFilename()
            .substring(profilePicture.getOriginalFilename().lastIndexOf(".") + 1);
        // 랜덤한 파일 이름 생성
        String fileName = UUID.randomUUID().toString() + "." + suffix;

        // 파일 업로드 경로 디렉토리가 만약 존재하지 않으면 생성
        File folder = new File(uploadFilePath);
        if (!folder.isDirectory()) {
            folder.mkdirs();
        }

        String pathName = uploadFilePath + fileName;    // 파일 절대 경로
        String resourcePathName = "/image/" + fileName; // url
        File dest = new File(pathName);
        try {
            profilePicture.transferTo(dest);
            user.modifyProfilePicture(fileName, pathName, resourcePathName, profilePicture);
        } catch (IllegalStateException | IOException e) {
            throw new ImageUploadFailException(); // 이미지 등록 실패 시 Exception
        }

        userRepository.save(user);
        log.info("UserService_modifyUserProfiler_end");
    }

    /**
     * 비밀번호 변경
     *
     * @param userId    userId
     * @param passwords 기존비밀번호, 새로운비밀번호
     */
    @Override
    public void modifyUserPassword(Long userId, UserModifyPasswordPutReq passwords) {
        log.info("UserService_modifyUserPrivacy_start: \nuserId: " + userId + "\npasswords: "
            + passwords.toString());
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        String oldPassword = passwords.getOldPassword();
        String newPassword = passwords.getNewPassword();

        // 비밀번호 일치 확인
        if (BCrypt.checkpw(oldPassword, user.getLoginPassword())) {
            String hashedNewPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            user.modifyUserPassword(hashedNewPassword);
            log.info("UserService_modifyUserPassword_end: success");
        } else { // 비밀번호 불일치
            log.info("UserService_modifyUserPassword_end: failed");
            throw new PasswordMismatchException();
        }
    }

    /**
     * 회원 탈퇴
     *
     * @param userId userId
     */
    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        user.addDeleteAt();
    }

    /**
     * 임시함수여서 지울듯
     *
     * @param userId userId
     * @param code   code
     */
    @Override
    public void setCode(Long userId, Integer code) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        MasterCode masterCode = masterCodeRepository.findById(code).orElseThrow(
            InvalidMasterCodeException::new);
        user.setCode(masterCode);
    }

    /**
     * 학생의 Solve Point 충전
     *
     * @param userId     userId
     * @param solvePoint 충전금액
     */
    @Override
    public void chargeSolvePoint(Long userId, Integer solvePoint) {
        Student student = studentRepository.findById(userId)
            .orElseThrow(UserNotFoundException::new);
        student.chargeSolvePoint(solvePoint);
    }

    /**
     * 강사의 Solve Point 출금
     *
     * @param userId     userId
     * @param solvePoint 출금금액
     */
    @Override
    public void cashOutSolvePoint(Long userId, Integer solvePoint) {
        Teacher teacher = teacherRepository.findById(userId)
            .orElseThrow(UserNotFoundException::new);
        teacher.cashOutSolvePoint(solvePoint);
    }

    /**
     * 학생의 강사 평점 입력
     *
     * @param ratingInfo 입력된 평점
     */
    @Override
    public void rateTeacher(TeacherRatePostReq ratingInfo) {
        Teacher teacher = teacherRepository.findById(ratingInfo.getUserId())
            .orElseThrow(UserNotFoundException::new);
        teacher.addRating(ratingInfo.getRating());
    }
}
