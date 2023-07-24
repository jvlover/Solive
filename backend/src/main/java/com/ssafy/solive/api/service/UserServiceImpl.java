package com.ssafy.solive.api.service;

import com.ssafy.solive.api.request.UserRegistPostReq;
import com.ssafy.solive.db.entity.User;
import com.ssafy.solive.db.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User registUser(UserRegistPostReq registInfo) {
        User user = registInfo.toDto();
        user.init();
        return userRepository.save(user);
    }

    @Override
    public User getUserByUserId(String userLoginId) {
        return userRepository.findByLoginId(userLoginId);
    }
}
