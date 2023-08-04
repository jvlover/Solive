package com.ssafy.solive.api.article.request;

import lombok.Data;

@Data
public class ArticleDeletePutReq {

    private Long userId;
    private Long articleId;
}
