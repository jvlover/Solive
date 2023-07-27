package com.ssafy.solive.api.controller;

import com.ssafy.solive.api.request.ArticleDeletePutReq;
import com.ssafy.solive.api.request.ArticleLikePostReq;
import com.ssafy.solive.api.request.ArticleModifyPutReq;
import com.ssafy.solive.api.request.ArticleRegistPostReq;
import com.ssafy.solive.api.request.ArticleReportPostReq;
import com.ssafy.solive.api.service.ArticleService;
import com.ssafy.solive.common.exception.ErrorCode;
import com.ssafy.solive.common.model.CommonResponse;
import com.ssafy.solive.db.entity.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
        @RequestPart(value = "files", required = false) MultipartFile[] files) {
        Article article = articleService.registArticle(registInfo, files);

        if (article != null) {
            return CommonResponse.success("success");
        } else {
            return CommonResponse.fail(ErrorCode.COMMON_SYSTEM_ERROR.getMessage(), "fail");
        }
    }

    @PostMapping("/like")
    public CommonResponse<?> like(@RequestBody ArticleLikePostReq likeInfo) {
        boolean isLiked = articleService.likeArticle(likeInfo);

        if (isLiked) {
            return CommonResponse.success("success");
        } else {
            return CommonResponse.fail(ErrorCode.COMMON_SYSTEM_ERROR.getMessage(), "fail");
        }
    }

    @PutMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public CommonResponse<?> modify(@RequestPart ArticleModifyPutReq modifyInfo,
        @RequestPart(value = "files", required = false) MultipartFile[] files) {
        boolean isModified = articleService.modifyArticle(modifyInfo, files);

        if (isModified) {
            return CommonResponse.success("success");
        } else {
            return CommonResponse.fail(ErrorCode.COMMON_SYSTEM_ERROR.getMessage(), "fail");
        }
    }

    @PutMapping("/delete")
    public CommonResponse<?> delete(@RequestBody ArticleDeletePutReq deleteInfo) {
        boolean isDeleted = articleService.deleteArticle(deleteInfo);

        if (isDeleted) {
            return CommonResponse.success("success");
        } else {
            return CommonResponse.fail(ErrorCode.COMMON_SYSTEM_ERROR.getMessage(), "fail");
        }
    }

    @PostMapping("/report")
    public CommonResponse<?> report(@RequestBody ArticleReportPostReq reportInfo) {
        boolean isReported = articleService.reportArticle(reportInfo);

        if (isReported) {
            return CommonResponse.success("success");
        } else {
            return CommonResponse.fail(ErrorCode.COMMON_SYSTEM_ERROR.getMessage(), "fail");
        }
    }
}
