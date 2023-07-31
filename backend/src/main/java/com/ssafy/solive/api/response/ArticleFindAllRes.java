package com.ssafy.solive.api.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArticleFindAllRes {

    private Long userId;
    private String title;
    private Long viewCount;
    private Long likeCount;
    private String time;
    private boolean hasPicture;
}
