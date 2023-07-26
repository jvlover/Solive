package com.ssafy.solive.common.exception;

public class QuestionDeleteFailException extends BaseException {

    public QuestionDeleteFailException() {
        super(ErrorCode.COMMON_SYSTEM_ERROR);
    }
}
