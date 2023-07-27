package com.ssafy.solive.common.exception;

public class QuestionNotFoundException extends BaseException {

    public QuestionNotFoundException() {
        super(ErrorCode.QUESTION_NOT_FOUND);
    }
}
