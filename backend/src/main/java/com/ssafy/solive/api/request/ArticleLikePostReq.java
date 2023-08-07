package com.ssafy.solive.api.request;

import lombok.Data;

@Data
public class ArticleLikePostReq {

    private Long userId;
    private Long articleId;
}
