package com.ssafy.solive.common.exception;

/*
 *  문제 등록 시 이미지가 없어서 등록 실패 Exception
 */
public class NoImageException extends BaseException {

    public NoImageException() {
        super(ErrorCode.NO_IMAGE);
    }
}
