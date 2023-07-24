package com.ssafy.solive.common.exception;

public class ImageNotFoundException extends BaseException {

    public ImageNotFoundException() {
        super(ErrorCode.IMAGE_NOT_FOUND);
    }
}
