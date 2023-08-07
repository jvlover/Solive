package com.ssafy.solive.common.exception.user;

import com.ssafy.solive.common.exception.ErrorCode;
import com.ssafy.solive.common.model.BaseException;

public class JwtTokenExpiredException extends BaseException {
    public JwtTokenExpiredException() {
        super(ErrorCode.JWT_TOKEN_EXPIRED_EXCEPTION);
    }
}
