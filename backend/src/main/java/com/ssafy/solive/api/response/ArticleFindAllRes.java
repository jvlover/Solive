package com.ssafy.solive.api.response;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArticleFindAllRes {

    // TODO: ArticleFindRes랑 통합
    private Long id;
    private String author;
    private String title;
    private String content;
    private Long viewCount;
    private Long likeCount;
    private Integer reportCount;
    private String time;
    private String lastUpdateTime;
    private List<String> articlePicturePathNames;
}
