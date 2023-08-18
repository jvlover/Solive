package com.ssafy.solive.common.exception.user;

import com.ssafy.solive.common.exception.ErrorCode;
import com.ssafy.solive.common.model.BaseException;

public class JwtInvalidException extends BaseException {

    public JwtInvalidException() {
        super(ErrorCode.INVAILD_JWT_TOKEN_EXCEPTION);
    }
}
