package com.ssafy.solive.api.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleLikePostReq {

    private Long userId;
    private Long articleId;
}
