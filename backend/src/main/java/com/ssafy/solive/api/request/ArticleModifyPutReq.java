package com.ssafy.solive.api.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleModifyPutReq {

    private Long userId;
    private Long articleId;
    private Integer masterCodeId;
    private String title;
    private String content;
}
