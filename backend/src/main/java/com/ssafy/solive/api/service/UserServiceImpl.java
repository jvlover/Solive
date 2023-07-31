package com.ssafy.solive.api.service;

import com.ssafy.solive.api.request.UserLoginPostReq;
import com.ssafy.solive.api.request.UserRegistPostReq;
import com.ssafy.solive.config.JwtConfiguration;
import com.ssafy.solive.db.entity.User;
import com.ssafy.solive.db.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return userRepository.save(user);
    }

    /**
     * 입력한 패스워드가 맞는지 확인 후 Refresh Token을 DB에 저장하고 Access Token을 반환
     *
     * @param loginInfo 로그인 한 Id, Password
     * @return accessToken 값
     */
    @Override
    public String loginAndGetAccessToken(UserLoginPostReq loginInfo) {
        String userLoginId = loginInfo.getLoginId();
        String userLoginPassword = loginInfo.getLoginPassword();
        User user = userRepository.findByLoginId(userLoginId);
        // 로그인 성공
        if (BCrypt.checkpw(userLoginPassword, user.getLoginPassword())) {
            // RefreshToken을 user DB에 저장
            user.updateRefreshToken(
                jwtConfiguration.createRefreshToken("userloginid", userLoginId));
            // AccessToken을 return
            return jwtConfiguration.createAccessToken("userloginid", userLoginId);
        } else {
            return null;
        }
    }
}
