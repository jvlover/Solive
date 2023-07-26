package com.ssafy.solive.api.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleModifyPutReq {

    private Long loginUserId;
    private Long articleId;
    private String title;
    private String content;
}
