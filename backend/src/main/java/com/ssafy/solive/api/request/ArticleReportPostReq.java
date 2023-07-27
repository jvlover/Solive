package com.ssafy.solive.api.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleReportPostReq {

    private Long userReportId;
    private Long userReportedId;
    private Long articleId;
    private String content;
}
