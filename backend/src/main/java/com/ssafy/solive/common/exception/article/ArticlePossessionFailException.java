package com.ssafy.solive.common.exception.article;

import com.ssafy.solive.common.exception.ErrorCode;
import com.ssafy.solive.common.model.BaseException;

public class ArticlePossessionFailException extends BaseException {

    public ArticlePossessionFailException() {
        super(ErrorCode.OUT_OF_POSSESSION);
    }
}
