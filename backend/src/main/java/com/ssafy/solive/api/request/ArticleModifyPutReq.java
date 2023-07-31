package com.ssafy.solive.api.request;

import lombok.Data;

@Data
public class ArticleModifyPutReq {

    private Long userId;
    private Long articleId;
    private Integer masterCodeId;
    private String title;
    private String content;
}
