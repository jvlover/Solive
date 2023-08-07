package com.ssafy.solive.common.exception;

public class InvalidMasterCodeException extends BaseException {

    public InvalidMasterCodeException() {
        super(ErrorCode.MASTER_CODE_NOT_FOUND);
    }
}
