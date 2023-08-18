package com.ssafy.solive.db.repository;

import com.ssafy.solive.api.user.response.TeacherOnlineGetRes;

import java.util.List;

public interface QTeacherRepository {
    public List<TeacherOnlineGetRes> findOnlineTeacher();
}
