package com.ssafy.solive.common.exception;

import com.ssafy.solive.common.model.BaseException;

public class InvalidMasterCodeException extends BaseException {

    public InvalidMasterCodeException() {
        super(ErrorCode.MASTER_CODE_NOT_FOUND);
    }
}
