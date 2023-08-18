package com.ssafy.solive.api.article.request;

import lombok.Data;

@Data
public class ArticleLikePostReq {

    private Long userId;
    private Long articleId;
}
