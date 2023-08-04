package com.ssafy.solive.api.user.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserPrivacyPostRes {

    String email;
    LocalDateTime signinTime;
}
