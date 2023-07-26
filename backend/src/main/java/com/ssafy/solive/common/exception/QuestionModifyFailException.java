package com.ssafy.solive.common.exception;

public class QuestionModifyFailException extends BaseException {

    public QuestionModifyFailException() {
        super(ErrorCode.COMMON_SYSTEM_ERROR);
    }
}
