package com.ssafy.solive.common.exception.matching;

import com.ssafy.solive.common.exception.ErrorCode;
import com.ssafy.solive.common.model.BaseException;

/*
 *  권한이 없는 문제에 수정 혹은 삭제 등을 요청했을 시 Exception
 */
public class QuestionPossessionFailException extends BaseException {

    public QuestionPossessionFailException() {
        super(ErrorCode.OUT_OF_POSSESSION);
    }
}
