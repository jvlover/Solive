package com.ssafy.solive.common.exception;

public class ImageUploadFailException extends BaseException {

    public ImageUploadFailException() {
        super(ErrorCode.IMAGE_UPLOAD_FAIL);
    }
}
