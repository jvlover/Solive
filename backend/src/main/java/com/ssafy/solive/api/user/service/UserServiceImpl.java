package com.ssafy.solive.api.user.service;

import com.ssafy.solive.api.user.request.TeacherRatePostReq;
import com.ssafy.solive.api.user.request.UserLoginPostReq;
import com.ssafy.solive.api.user.request.UserModifyPasswordPutReq;
import com.ssafy.solive.api.user.request.UserModifyProfilePutReq;
import com.ssafy.solive.api.user.request.UserRegistPostReq;
import com.ssafy.solive.api.user.response.TeacherOnlineGetRes;
import com.ssafy.solive.api.user.response.UserLoginPostRes;
import com.ssafy.solive.api.user.response.UserPrivacyPostRes;
import com.ssafy.solive.api.user.response.UserProfilePostRes;
import com.ssafy.solive.common.exception.InvalidMasterCodeException;
import com.ssafy.solive.common.exception.user.DuplicatedEmailException;
import com.ssafy.solive.common.exception.user.DuplicatedLoginIdException;
import com.ssafy.solive.common.exception.user.DuplicatedNicknameException;
import com.ssafy.solive.common.exception.user.FavoriteNotFoundException;
import com.ssafy.solive.common.exception.user.InvalidJwtRefreshTokenException;
import com.ssafy.solive.common.exception.user.JwtTokenExpiredException;
import com.ssafy.solive.common.exception.user.PasswordMismatchException;
import com.ssafy.solive.common.exception.user.UserNotFoundException;
import com.ssafy.solive.common.model.FileDto;
import com.ssafy.solive.common.util.FileUploader;
import com.ssafy.solive.config.JwtConfiguration;
import com.ssafy.solive.db.entity.Favorite;
import com.ssafy.solive.db.entity.FavoriteId;
import com.ssafy.solive.db.entity.MasterCode;
import com.ssafy.solive.db.entity.Student;
import com.ssafy.solive.db.entity.Teacher;
import com.ssafy.solive.db.entity.User;
import com.ssafy.solive.db.repository.FavoriteRepository;
import com.ssafy.solive.db.repository.MasterCodeRepository;
import com.ssafy.solive.db.repository.StudentRepository;
import com.ssafy.solive.db.repository.TeacherRepository;
import com.ssafy.solive.db.repository.UserRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Transactional
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final MasterCodeRepository masterCodeRepository;
    private final FavoriteRepository favoriteRepository;
    private final JwtConfiguration jwtConfiguration;
    private final FileUploader fileUploader;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, StudentRepository studentRepository,
        TeacherRepository teacherRepository, MasterCodeRepository masterCodeRepository,
        FavoriteRepository favoriteRepository, JwtConfiguration jwtConfiguration,
        FileUploader fileUploader) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.masterCodeRepository = masterCodeRepository;
        this.favoriteRepository = favoriteRepository;
        this.jwtConfiguration = jwtConfiguration;
        this.fileUploader = fileUploader;
    }

    /**
     * @param registInfo 회원가입 정보
     */
    @Override
    public void registUser(UserRegistPostReq registInfo) {
        log.info("UserService_registUser_start: " + registInfo.toString());
        // 비밀번호에 Bcrypt 적용
        String hashedPassword = BCrypt.hashpw(registInfo.getLoginPassword(), BCrypt.gensalt());
        // 마스터 코드 객체 생성
        MasterCode masterCode = masterCodeRepository.findById(registInfo.getMasterCodeId())
            .orElseThrow(InvalidMasterCodeException::new);
        MasterCode logoutState = masterCodeRepository.findById(11) // 로그아웃 상태로 초기화
            .orElseThrow(InvalidMasterCodeException::new);

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

            if (userRepository.existsByLoginId(registInfo.getLoginId())) {
                throw new DuplicatedLoginIdException();
            } else if (userRepository.existsByEmail(registInfo.getEmail())) {
                throw new DuplicatedEmailException();
            } else if (userRepository.existsByNickname(registInfo.getNickname())) {
                throw new DuplicatedNicknameException();
            } else {
                studentRepository.save(student);
                log.info("UserService_registUser_end: " + student);
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

            if (userRepository.existsByLoginId(registInfo.getLoginId())) {
                throw new DuplicatedLoginIdException();
            } else if (userRepository.existsByEmail(registInfo.getEmail())) {
                throw new DuplicatedEmailException();
            } else if (userRepository.existsByNickname(registInfo.getNickname())) {
                throw new DuplicatedNicknameException();
            } else {
                teacherRepository.save(teacher);
                log.info("UserService_registUser_end: " + teacher);
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
            String accessToken = jwtConfiguration.createAccessToken("userId", userId);
            String refreshToken = jwtConfiguration.createRefreshToken("userId", userId);

            user.setLoginState(masterCodeRepository.findById(12)
                .orElseThrow(InvalidMasterCodeException::new)); // 로그인 상태로 변경
            // RefreshToken을 user DB에 저장
            user.updateRefreshToken(refreshToken);

            // Token들을 return
            UserLoginPostRes userLoginPostRes = UserLoginPostRes.builder()
                .userId(userId)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .masterCodeId(user.getMasterCodeId().getId())
                .nickname(user.getNickname())
                .solvePoint(user.getSolvePoint())
                .path(user.getPath())
                .build();
            log.info("UserService_loginAndGetAccessToken_end: " + userLoginPostRes);
            return userLoginPostRes;
        } else { // 로그인 실패
            log.info("UserService_loginAndGetAccessToken_end: PasswordMismatchException");
            throw new PasswordMismatchException();
        }
    }

    @Override
    public void logout(Long userId) {
        log.info("UserService_logout_start: " + userId);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        MasterCode stateId = masterCodeRepository.findById(11) // 로그아웃 상태 MasterCode 가져오기
            .orElseThrow(InvalidMasterCodeException::new);
        user.logout(stateId);
        log.info("UserService_logout_end");
    }

    /**
     * accessToken 으로 userId를 조회
     *
     * @param accessToken accessToken
     * @return userId
     */
    @Override
    public Long getUserIdByToken(String accessToken) {
        log.info("UserService_getUserIdByToken_start: " + accessToken);
        if (jwtConfiguration.checkToken(accessToken)) { // accessToken 이 유효할 때
            Long userId = jwtConfiguration.getUserId(accessToken);
            log.info("UserService_getUserIdByToken_end: " + userId);
            return userId;
        } else {
            log.info("UserService_getUserIdByToken_end: JwtTokenExpiredException");
            throw new JwtTokenExpiredException();
        }
    }

    @Override
    public String recreateAccessToken(Long userId, String refreshToken) {
        log.info("UserService_recreateAccessToken_start: " + userId + ", " + refreshToken);
        if (jwtConfiguration.checkToken(refreshToken)) { // refreshToken 유효
            String dbRefreshToken = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new).getRefreshToken();

            if (dbRefreshToken.equals(refreshToken)) { // refreshToken, DB의 refreshToken 일치
                String accessToken = jwtConfiguration.createAccessToken("userId", userId);
                log.info("UserService_recreateAccessToken_end: " + accessToken);
                return accessToken;
            } else { // 받은 refreshToken, db의 refreshToken 정보가 다를 때
                log.info("UserService_recreateAccessToken_end: InvalidJwtRefreshTokenException");
                throw new InvalidJwtRefreshTokenException();
            }
        } else { // refreshToken 만료
            log.info("UserService_recreateAccessToken_end: JwtTokenExpiredException");
            throw new JwtTokenExpiredException();
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
        log.info("UserServiceImpl_getUserProfileByUserId_start: " + userId);

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Integer type = user.getMasterCodeId().getId();
        if (type == 1) { // 학생일땐
            Student student = studentRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
            UserProfilePostRes userProfilePostRes = UserProfilePostRes.builder()
                .fileName(user.getFileName())
                .originalName(user.getOriginalName())
                .path(user.getPath())
                .contentType(user.getContentType())
                .nickname(user.getNickname())
                .gender(user.getGender())
                .experience(user.getExperience())
                .introduce(user.getIntroduce())
                .questionCount(student.getQuestionCount()) // 학생일때만
                .build();

            log.info("UserServiceImpl_getUserProfileByUserId_end: student: " + userProfilePostRes);
            return userProfilePostRes;
        } else if (type == 2) {
            Teacher teacher = teacherRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
            UserProfilePostRes userProfilePostRes = UserProfilePostRes.builder()
                .fileName(user.getFileName())
                .originalName(user.getOriginalName())
                .path(user.getPath())
                .contentType(user.getContentType())
                .nickname(user.getNickname())
                .gender(user.getGender())
                .experience(user.getExperience())
                .introduce(user.getIntroduce())
                .solvedCount(teacher.getSolvedCount()) // 선생님일때만
                .ratingSum(teacher.getRatingSum()) // 선생님일때만
                .ratingCount(teacher.getRatingCount()) // 선생님일때만
                .build();
            log.info("UserServiceImpl_getUserProfileByUserId_end: teacher: " + userProfilePostRes);
            return userProfilePostRes;
        }
        log.info("UserServiceImpl_getUserProfileByUserId_end: InvalidMasterCodeException");
        throw new InvalidMasterCodeException();
    }

    /**
     * userId로 개인정보 조회
     *
     * @param userId userId
     * @return UserPrivacyPostRes
     */
    @Override
    public UserPrivacyPostRes getUserPrivacyByUserId(Long userId) {
        log.info("UserServiceImpl_getUserPrivacyByUserId_start" + userId);

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        UserPrivacyPostRes userPrivacyPostRes = UserPrivacyPostRes.builder()
            .email(user.getEmail())
            .signinTime(user.getSigninTime())
            .build();

        log.info("UserServiceImpl_getUserPrivacyByUserId_end" + userPrivacyPostRes);
        return userPrivacyPostRes;
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
        List<MultipartFile> profilePicture) { // List 로 받지만 length 1
        log.info("UserService_modifyUserProfile_start: " + userId + ", "
            + userInfo.toString() + ", " + profilePicture);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        user.modifyUserProfile(userInfo);
        log.info("UserService_modifyUserProfile_mid: " + user);

        if (profilePicture != null) { // 저장할 프로필 사진이 있으면
            // 프로필 사진 정보 수정, 프로필 사진은 항상 length 1
            FileDto fileDto = fileUploader.fileUpload(profilePicture, "/profile").get(0);
            user.modifyProfilePicture(fileDto);
        }

        log.info("UserService_modifyUserProfile_end");
    }

    /**
     * 비밀번호 변경
     *
     * @param userId    userId
     * @param passwords 기존비밀번호, 새로운비밀번호
     */
    @Override
    public void modifyUserPassword(Long userId, UserModifyPasswordPutReq passwords) {
        log.info("UserService_modifyUserPassword_start: " + userId + ", "
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
            log.info("UserService_modifyUserPassword_end: PasswordMismatchException");
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
        log.info("UserService_deleteUser_start: " + userId);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        user.addDeleteAt();
        log.info("UserService_deleteUser_end");
    }

    /**
     * 학생의 Solve Point 충전
     *
     * @param userId     userId
     * @param solvePoint 충전금액
     */
    @Override
    public void chargeSolvePoint(Long userId, Integer solvePoint) {
        log.info("UserService_chargeSolvePoint_start: " + userId + ", " + solvePoint);
        Student student = studentRepository.findById(userId)
            .orElseThrow(UserNotFoundException::new);
        student.chargeSolvePoint(solvePoint);
        log.info("UserService_chargeSolvePoint_end");
    }

    /**
     * 강사의 Solve Point 출금
     *
     * @param userId     userId
     * @param solvePoint 출금금액
     */
    @Override
    public void cashOutSolvePoint(Long userId, Integer solvePoint) {
        log.info("UserService_cashOutSolvePoint_start: " + userId + ", " + solvePoint);
        Teacher teacher = teacherRepository.findById(userId)
            .orElseThrow(UserNotFoundException::new);
        teacher.cashOutSolvePoint(solvePoint);
        log.info("UserService_cashOutSolvePoint_end");
    }

    /**
     * 학생의 강사 평점 입력
     *
     * @param ratingInfo 입력된 평점
     */
    @Override
    public void rateTeacher(TeacherRatePostReq ratingInfo) {
        log.info("UserService_rateTeacher_start: " + ratingInfo);
        Teacher teacher = teacherRepository.findById(ratingInfo.getTeacherId())
            .orElseThrow(UserNotFoundException::new);
        teacher.addRating(ratingInfo.getRating());
        log.info("UserService_rateTeacher_end");
    }

    @Override
    public void addFavorite(Long studentId, Long teacherId) {
        log.info("UserService_addFavorite_start: " + studentId + ", " + teacherId);

        FavoriteId favoriteId = FavoriteId.builder()
            .studentId(studentId)
            .teacherId(teacherId)
            .build();

        Favorite favorite = favoriteRepository.findById(favoriteId).orElse(null);
        log.info("UserService_addFavorite_mid: " + favorite);

        if (favorite == null) { // 즐겨찾기 정보가 없는 경우 -> 새 Data 만들어서 등록
            Student student = studentRepository.findById(studentId)
                .orElseThrow(UserNotFoundException::new);
            Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(UserNotFoundException::new);

            favoriteRepository.save(Favorite.builder()
                .studentId(student)
                .teacherId(teacher)
                .build());
        } else { // 즐겨찾기 정보가 DB에 있지만 다시 등록하는 경우 -> Time Column 갱신
            favorite.addTime();
        }

        log.info("UserService_addFavorite_end: success");
    }

    @Override
    public void deleteFavorite(Long studentId, Long teacherId) {
        log.info("UserService_deleteFavorite_start: " + studentId + ", " + teacherId);
        FavoriteId favoriteId = FavoriteId.builder()
            .studentId(studentId)
            .teacherId(teacherId)
            .build();

        Favorite favorite = favoriteRepository.findById(favoriteId)
            .orElseThrow(FavoriteNotFoundException::new);

        favorite.addDeleteAt();
        log.info("UserService_deleteFavorite_end");
    }

    @Override
    public boolean isLogout(String accessToken) {
        log.info("UserService_isLogout_start: " + accessToken);
        Long userId = getUserIdByToken(accessToken);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        if (user.getMasterCodeId().getId() == 11) { // 로그아웃 이면
            log.info("UserService_isLogout_end: true");
            return true;
        } else {
            log.info("UserService_isLogout_end: false");
            return false;
        }
    }

    @Override
    public List<TeacherOnlineGetRes> getOnlineTeacher() {
        log.info("UserService_getOnlineTeacher_start");
        List<TeacherOnlineGetRes> teacherOnlineGetResList = teacherRepository.findOnlineTeacher();

        log.info("UserService_getOnlineTeacher_end: " + teacherOnlineGetResList);
        return teacherOnlineGetResList;
    }
}
