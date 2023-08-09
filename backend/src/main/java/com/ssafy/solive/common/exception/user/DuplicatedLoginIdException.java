package com.ssafy.solive.common.exception.user;

import com.ssafy.solive.common.exception.ErrorCode;
import com.ssafy.solive.common.model.BaseException;

public class DuplicatedLoginIdException extends BaseException {
    public DuplicatedLoginIdException() {
        super(ErrorCode.DUPLICATED_LOGIN_ID);
    }
}
