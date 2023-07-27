package com.ssafy.solive.api.request;

import lombok.Data;

@Data
public class ArticleReportPostReq {

    private Long userReportId;
    private Long userReportedId;
    private Long articleId;
    private String content;
}
