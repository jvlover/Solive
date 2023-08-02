package com.ssafy.solive.api.service;

import com.ssafy.solive.api.request.UserLoginPostReq;
import com.ssafy.solive.api.request.UserModifyPutReq;
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

    @Override
    public User registUser(UserRegistPostReq registInfo) {
        log.info("UserService_registUser_start: " + registInfo.toString());
        // 비밀번호에 Bcrypt 적용
        String hashedPassword = BCrypt.hashpw(registInfo.getLoginPassword(), BCrypt.gensalt());
        // 마스터 코드 객체 생성
        MasterCode masterCode = masterCodeRepository.findById(registInfo.getMasterCodeId()).get();

        User user = User.builder()
            .loginId(registInfo.getLoginId())
            .loginPassword(hashedPassword)
            .masterCodeId(masterCode)
            .nickname(registInfo.getNickname())
            .email(registInfo.getEmail())
            .gender(registInfo.getGender())
            .build();
        log.info("UserService_registUser_end: " + user.toString());

        try {
            userRepository.save(user);
            log.info("UserService_registUser_end: " + user.toString());
            return user;
        } catch (Exception e) {
            // TODO: 예외 관련 세분화 처리 ex) 중복확인, 미가입 유저 등
            e.printStackTrace();
            log.info("UserService_registUser_end: null");
            return null;
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
                .introduce(user.getIntroduce())
                .experience(user.getExperience())
                .build();
        } else {
            // TODO: Exception
            return null;
        }
    }

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

    @Override
    public void modifyUser(Long userId, UserModifyPutReq userInfo) {
        log.info("UserService_modifyUser_start: \nuserId: " + userId + "\nuserInfo: "
            + userInfo.toString());
        User user = userRepository.findById(userId).get();
        log.info("UserService_modifyUser_mid: \nuser: " + user.toString());
        user.modifyUser(userInfo);

        log.info("UserService_modifyUser_mid: \nmodifiedUser: " + user.toString());

        userRepository.save(user);
        log.info("UserService_modifyUser_end");
    }

    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId).get();
        user.addDeleteAt();
    }

    @Override
    public void setCode(Long userId, Integer code) {
        User user = userRepository.findById(userId).get();
        MasterCode masterCode = masterCodeRepository.findById(code).get();
        user.setCode(masterCode);
    }

    @Override
    public void chargeSolvePoint(Long userId, Integer solvePoint) {
        Student student = studentRepository.findById(userId).get();
        student.chargeSolvePoint(solvePoint);
    }

    @Override
    public void cashOutSolvePoint(Long userId, Integer solvePoint) {
        Teacher teacher = teacherRepository.findById(userId).get();
        teacher.cashOutSolvePoint(solvePoint);
    }
}
