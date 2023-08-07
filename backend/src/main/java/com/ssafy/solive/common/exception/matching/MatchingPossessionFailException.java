package com.ssafy.solive.common.exception.matching;

import com.ssafy.solive.common.exception.ErrorCode;
import com.ssafy.solive.common.model.BaseException;

/*
 *  매칭 시 권한이 없는 상황에서 수정 혹은 삭제 등을 요청했을 시 Exception
 */
public class MatchingPossessionFailException extends BaseException {

    public MatchingPossessionFailException() {
        super(ErrorCode.OUT_OF_POSSESSION);
    }
}
