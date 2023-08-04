package com.ssafy.solive.common.exception.user;

import com.ssafy.solive.common.exception.ErrorCode;
import com.ssafy.solive.common.model.BaseException;

public class PasswordMismatchException extends BaseException {

    public PasswordMismatchException() {
        super(ErrorCode.PASSWORD_NOT_MATCHED);
    }
}
