package com.ssafy.solive.common.exception.user;

import com.ssafy.solive.common.exception.ErrorCode;
import com.ssafy.solive.common.model.BaseException;

public class InvalidJwtRefreshTokenException extends BaseException {

    public InvalidJwtRefreshTokenException() {
        super(ErrorCode.REFRESH_TOKEN_NOT_MATCHED);
    }
}
