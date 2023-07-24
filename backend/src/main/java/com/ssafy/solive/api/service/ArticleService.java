package com.ssafy.solive.api.service;

import com.ssafy.solive.api.request.ArticleRegistPostReq;
import com.ssafy.solive.db.entity.Article;
import org.springframework.web.multipart.MultipartFile;

public interface ArticleService {

    public Article registArticle(ArticleRegistPostReq registInfo, MultipartFile[] files);
}
