package com.ssafy.solive.api.service;

import com.ssafy.solive.api.request.ArticleDeletePutReq;
import com.ssafy.solive.api.request.ArticleModifyPutReq;
import com.ssafy.solive.api.request.ArticleRegistPostReq;
import com.ssafy.solive.db.entity.Article;
import org.springframework.web.multipart.MultipartFile;

public interface ArticleService {

    Article registArticle(ArticleRegistPostReq registInfo, MultipartFile[] files);

    boolean deleteArticle(ArticleDeletePutReq deleteInfo);

    boolean modifyArticle(ArticleModifyPutReq modifyInfo, MultipartFile[] files);
}
