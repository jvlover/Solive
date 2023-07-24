package com.ssafy.solive.common.exception;

public class BaseException extends RuntimeException {

    private final ErrorCode errorCode;

    public BaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    /*
     * Errorcode 외에 별도의 메시지가 필요할 경우 추가하여 작성 가능
     * 쓸 일이 얼마나 있을 지는 솔직히 모르겠음
     */
    public BaseException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
