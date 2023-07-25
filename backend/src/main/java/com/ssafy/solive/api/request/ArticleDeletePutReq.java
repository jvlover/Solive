package com.ssafy.solive.api.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleDeletePutReq {

    private Long loginUserId;
    private Long articleId;
}
