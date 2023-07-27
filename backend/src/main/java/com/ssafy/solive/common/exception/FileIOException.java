package com.ssafy.solive.common.exception;

public class FileIOException extends BaseException {

    public FileIOException() {
        super(ErrorCode.FILE_IO_ERROR);
    }
}
