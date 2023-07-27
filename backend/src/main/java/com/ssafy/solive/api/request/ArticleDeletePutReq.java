package com.ssafy.solive.api.request;

import lombok.Data;

@Data
public class ArticleDeletePutReq {

    private Long loginUserId;
    private Long articleId;
}
