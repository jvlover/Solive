package com.ssafy.solive.common.exception;

import com.ssafy.solive.common.model.BaseException;

public class NoDataException extends BaseException {

    public NoDataException() {
        super(ErrorCode.NO_SUCH_DATA);
    }
}
