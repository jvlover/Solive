package com.ssafy.solive.api.service;

import com.ssafy.solive.api.request.UserLoginPostReq;
import com.ssafy.solive.api.request.UserRegistPostReq;
import com.ssafy.solive.config.JwtConfiguration;
import com.ssafy.solive.db.entity.User;
import com.ssafy.solive.db.repository.UserRepository;
import jakarta.transaction.Transactional;
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
        User user = registInfo.toUser();
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
        /* TODO: 암호화 후 아래 코드 개선해 사용
         * 암호화 적용 전, 비밀번호 단순 비교
         */
        // 로그인 성공
        if (user.getLoginPassword().equals(userLoginPassword)) {
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
