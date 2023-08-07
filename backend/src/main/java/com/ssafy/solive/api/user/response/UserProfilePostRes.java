package com.ssafy.solive.api.user.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfilePostRes {

    String fileName;
    String originalName;
    String path;
    String contentType;
    String nickname;
    Integer gender;
    Long experience;
    String introduce;
}
