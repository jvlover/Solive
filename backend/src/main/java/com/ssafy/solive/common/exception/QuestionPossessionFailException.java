package com.ssafy.solive.common.exception;

public class QuestionPossessionFailException extends BaseException {

    public QuestionPossessionFailException() {
        super(ErrorCode.OUT_OF_POSSESSION);
    }
}
