package com.ssafy.solive.common.exception.matching;

import com.ssafy.solive.common.exception.ErrorCode;
import com.ssafy.solive.common.model.BaseException;

public class NotEnoughPointException extends BaseException {

    public NotEnoughPointException() {
        super(ErrorCode.NOT_ENOUGH_POINT);
    }
}
