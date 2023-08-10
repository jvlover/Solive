package com.ssafy.solive.api.user.response;

import lombok.Data;

@Data
public class TeacherOnlineGetRes {
    String path;
    String nickname;
    String masterCodeName;
    Double rating;

    public TeacherOnlineGetRes() {
    }

    public TeacherOnlineGetRes(String path, String nickname, String masterCodeName, Double rating) {
        this.path = path;
        this.nickname = nickname;
        this.masterCodeName = masterCodeName;
        this.rating = rating;
    }
}
