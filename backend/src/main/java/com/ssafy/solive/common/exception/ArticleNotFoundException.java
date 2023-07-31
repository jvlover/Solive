package com.ssafy.solive.common.exception;

public class ArticleNotFoundException extends BaseException {

    public ArticleNotFoundException() {
        super(ErrorCode.ARTICLE_NOT_FOUND);
    }
}
