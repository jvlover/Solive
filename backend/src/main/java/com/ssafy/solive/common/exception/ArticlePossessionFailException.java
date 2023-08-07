package com.ssafy.solive.common.exception;

public class ArticlePossessionFailException extends BaseException {

    public ArticlePossessionFailException() {
        super(ErrorCode.OUT_OF_POSSESSION);
    }
}
