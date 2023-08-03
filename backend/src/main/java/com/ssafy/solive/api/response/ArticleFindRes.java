package com.ssafy.solive.api.response;

import java.util.List;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ArticleFindRes {

    private Long id; // 게시글 id
    private Long userId; // 유저 id
    private String author; // 유저 nickname
    private String title;
    private String content;
    private Long viewCount;
    private Long likeCount;
    private Integer reportCount;
    private String time;
    private String lastUpdateTime;
    private List<String> articlePicturePathNames;
}
