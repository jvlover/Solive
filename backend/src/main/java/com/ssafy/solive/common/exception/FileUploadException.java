package com.ssafy.solive.common.exception;

public class FileUploadException extends BaseException {

    public FileUploadException() {
        super(ErrorCode.FILE_IO_ERROR);
    }
}
