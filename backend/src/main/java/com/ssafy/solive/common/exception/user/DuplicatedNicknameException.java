package com.ssafy.solive.common.exception.user;

import com.ssafy.solive.common.exception.ErrorCode;
import com.ssafy.solive.common.model.BaseException;

public class DuplicatedNicknameException extends BaseException {
    public DuplicatedNicknameException() {
        super(ErrorCode.DUPLICATED_NICKNAME);
    }
}
