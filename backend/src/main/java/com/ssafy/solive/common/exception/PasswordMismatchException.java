package com.ssafy.solive.common.exception;

public class PasswordMismatchException extends BaseException {

    public PasswordMismatchException() {
        super(ErrorCode.PASSWORD_NOT_MATCHED);
    }
}
