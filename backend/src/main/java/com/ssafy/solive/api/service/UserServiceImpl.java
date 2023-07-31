package com.ssafy.solive.api.service;

import com.ssafy.solive.api.request.UserLoginPostReq;
import com.ssafy.solive.api.request.UserRegistPostReq;
import com.ssafy.solive.common.exception.PasswordMismatchException;
import com.ssafy.solive.config.JwtConfiguration;
import com.ssafy.solive.db.entity.User;
import com.ssafy.solive.db.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Transactional
@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private JwtConfiguration jwtConfiguration;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, JwtConfiguration jwtConfiguration) {
        this.userRepository = userRepository;
        this.jwtConfiguration = jwtConfiguration;
    }

    @Override
    public User registUser(UserRegistPostReq registInfo) {
        log.info("UserService_registUser_start: " + registInfo.toString());
        // 비밀번호에 Bcrypt 적용
        String hashedPassword = BCrypt.hashpw(registInfo.getLoginPassword(), BCrypt.gensalt());
        User user = User.builder()
            .loginId(registInfo.getLoginId())
            .loginPassword(hashedPassword)
            .masterCodeId(registInfo.getMasterCodeId())
            .nickname(registInfo.getNickname())
            .email(registInfo.getEmail())
            .pictureUrl(registInfo.getPictureUrl())
            .pictureName(registInfo.getPictureName())
            .introduce(registInfo.getIntroduce())
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
     * @param loginInfo 로그인 한 Id, Password
     * @return accessToken 값
     */
    @Override
    public String loginAndGetAccessToken(UserLoginPostReq loginInfo) {
        log.info("UserService_loginAndGetAccessToken_start: " + loginInfo.toString());
        String userLoginId = loginInfo.getLoginId();
        String userLoginPassword = loginInfo.getLoginPassword();
        User user = userRepository.findByLoginId(userLoginId);
        // 로그인 성공
        if (BCrypt.checkpw(userLoginPassword, user.getLoginPassword())) {
            // RefreshToken을 user DB에 저장
            user.updateRefreshToken(
                jwtConfiguration.createRefreshToken("userloginid", userLoginId));
            // AccessToken을 return
            String accessToken = jwtConfiguration.createAccessToken("userloginid", userLoginId);
            log.info("UserService_loginAndGetAccessToken_end: " + accessToken);
            return accessToken;
        } else {
            throw new PasswordMismatchException();
        }
    }
}
