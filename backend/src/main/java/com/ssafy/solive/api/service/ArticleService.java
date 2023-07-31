package com.ssafy.solive.api.service;

import com.ssafy.solive.api.request.ArticleDeletePutReq;
import com.ssafy.solive.api.request.ArticleLikePostReq;
import com.ssafy.solive.api.request.ArticleModifyPutReq;
import com.ssafy.solive.api.request.ArticleRegistPostReq;
import com.ssafy.solive.api.request.ArticleReportPostReq;
import com.ssafy.solive.api.response.ArticleFindAllRes;
import com.ssafy.solive.api.response.ArticleFindRes;
import com.ssafy.solive.db.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface ArticleService {

    Article registArticle(ArticleRegistPostReq registInfo, MultipartFile[] files);

    boolean deleteArticle(ArticleDeletePutReq deleteInfo);

    boolean modifyArticle(ArticleModifyPutReq modifyInfo, MultipartFile[] files);

    boolean likeArticle(ArticleLikePostReq likeInfo);

    boolean reportArticle(ArticleReportPostReq reportInfo);

    ArticleFindRes findArticle(Long articleId);

    Page<ArticleFindAllRes> findAllArticle(String keyword, Pageable pageable);
}
