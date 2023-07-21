package com.ssafy.solive.api.service;

import com.ssafy.solive.api.request.UserRegistPostReq;
import com.ssafy.solive.db.entity.User;

public interface UserService {

    public User registUser(UserRegistPostReq registInfo);

    User getUserByUserId(String userId);
}
