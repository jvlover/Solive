package com.ssafy.solive.common.exception;

import com.ssafy.solive.common.model.BaseException;

public class FileIOException extends BaseException {

    public FileIOException() {
        super(ErrorCode.FILE_IO_ERROR);
    }
}
