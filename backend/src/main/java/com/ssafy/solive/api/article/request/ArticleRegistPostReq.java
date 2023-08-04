package com.ssafy.solive.api.article.request;

import lombok.Data;

@Data
public class ArticleRegistPostReq {

    private Long userId;
    private Integer masterCodeId;
    private String title;
    private String content;
}
