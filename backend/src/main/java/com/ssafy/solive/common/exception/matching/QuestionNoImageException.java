package com.ssafy.solive.common.exception.matching;

import com.ssafy.solive.common.exception.ErrorCode;
import com.ssafy.solive.common.model.BaseException;

/*
 *  문제 등록 시 이미지가 없어서 등록 실패 Exception
 */
public class QuestionNoImageException extends BaseException {

    public QuestionNoImageException() {
        super(ErrorCode.NO_IMAGE);
    }
}
