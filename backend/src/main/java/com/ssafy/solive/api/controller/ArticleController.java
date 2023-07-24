package com.ssafy.solive.api.controller;

import com.ssafy.solive.api.request.ArticleRegistPostReq;
import com.ssafy.solive.api.service.ArticleService;
import com.ssafy.solive.common.model.CommonResponse;
import com.ssafy.solive.db.entity.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/board")
public class ArticleController {

    ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public CommonResponse<?> regist(@RequestPart ArticleRegistPostReq registInfo,
        @RequestPart("files") MultipartFile[] files) {
        Article article = articleService.registArticle(registInfo, files);

        if (article != null) {
            return CommonResponse.success("success");
        } else {
            return CommonResponse.fail(null, "fail");
        }
    }
}
