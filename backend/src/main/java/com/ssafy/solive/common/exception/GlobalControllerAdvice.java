package com.ssafy.solive.common.exception;

import com.ssafy.solive.common.model.CommonResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(BaseException.class)
    public CommonResponse<?> onBaseException(BaseException e) {

        return CommonResponse.fail(e.getErrorCode().name(), e.getMessage());
    }
}
