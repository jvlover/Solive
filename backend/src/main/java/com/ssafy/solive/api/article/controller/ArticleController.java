package com.ssafy.solive.api.article.controller;

import com.ssafy.solive.api.article.request.*;
import com.ssafy.solive.api.article.response.ArticleFindRes;
import com.ssafy.solive.api.article.service.ArticleService;
import com.ssafy.solive.common.exception.FileIOException;
import com.ssafy.solive.common.exception.article.ArticleNotFoundException;
import com.ssafy.solive.common.exception.article.ArticlePossessionFailException;
import com.ssafy.solive.common.model.CommonResponse;
import com.ssafy.solive.db.entity.Article;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 유저가 게시판을 이용할 때 필요한 API를 모은 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/board")
public class ArticleController {

    private static final String SUCCESS = "success"; // API 성공 시 return

    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    /**
     * 유저가 게시글을 등록하기 위한 API
     *
     * @param registInfo : 게시글 등록할 때 입력한 정보
     * @param fileList   : 게시글 사진, 게시글에는 사진이 반드시 있을 필요가 없음
     */
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public CommonResponse<?> regist(@RequestPart ArticleRegistPostReq registInfo,
        @RequestPart(value = "files", required = false) List<MultipartFile> fileList) {

        if (fileList != null) { // 게시물에 파일 있으면
            log.info("ArticleController_regist_start: " + registInfo.toString() + ", "
                + fileList);
        } else {
            log.info("ArticleController_regist_start: " + registInfo.toString());
        }

        Article article = articleService.registArticle(registInfo, fileList);
        if (article != null) {  // regist 성공하면 success
            log.info("ArticleController_regist_end: success");
            return CommonResponse.success(SUCCESS);
        } else {    // 실패하면 Exception
            throw new FileIOException();
        }
    }

    /**
     * 유저가 게시글을 좋아요하기 위한 API
     *
     * @param likeInfo : 게시글 좋아요를 하기 위해 필요한 정보
     */
    @PostMapping("/like")
    public CommonResponse<?> like(@RequestBody ArticleLikePostReq likeInfo) {

        log.info("ArticleController_like_start: " + likeInfo.toString());

        boolean isLiked = articleService.likeArticle(likeInfo);

        if (isLiked) {  // 좋아요 성공하면 success
            log.info("ArticleController_like_end: success");
            return CommonResponse.success(SUCCESS);
        } else {    // 좋아요 실패하면 Exception
            throw new ArticlePossessionFailException();
        }
    }

    /**
     * 유저가 게시글을 수정하기 위한 API
     *
     * @param modifyInfo : 게시글 수정할 때 입력한 정보
     * @param fileList   : 게시글 사진, 게시글에는 사진이 반드시 있을 필요가 없음
     */
    @PutMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public CommonResponse<?> modify(@RequestPart ArticleModifyPutReq modifyInfo,
        @RequestPart(value = "files", required = false) List<MultipartFile> fileList) {

        if (fileList != null) {
            log.info("ArticleController_modify_start: " + modifyInfo.toString() + ", "
                + fileList);
        } else {
            log.info("ArticleController_modify_start: " + modifyInfo.toString());
        }
        boolean isModified = articleService.modifyArticle(modifyInfo, fileList);

        if (isModified) {   // 수정 성공하면 success
            log.info("ArticleController_modify_end: success");
            return CommonResponse.success(SUCCESS);
        } else {    // 수정 실패하면 Exception
            throw new ArticlePossessionFailException();
        }
    }

    /**
     * 유저가 게시글을 삭제하기 위한 API
     *
     * @param deleteInfo : 게시글을 삭제하기 위해 필요한 정보
     */
    @PutMapping("/delete")
    public CommonResponse<?> delete(@RequestBody ArticleDeletePutReq deleteInfo) {

        log.info("ArticleController_delete_start: " + deleteInfo.toString());

        boolean isDeleted = articleService.deleteArticle(deleteInfo);

        if (isDeleted) {    // 삭제 성공하면 success
            log.info("ArticleController_delete_end: success");
            return CommonResponse.success(SUCCESS);
        } else {    // 삭제 실패하면 Exception
            throw new ArticlePossessionFailException();
        }
    }

    /**
     * 유저가 게시글을 신고하기 위한 API
     *
     * @param reportInfo : 게시글을 신고하기 위해 필요한 정보
     */
    @PostMapping("/report")
    public CommonResponse<?> report(@RequestBody ArticleReportPostReq reportInfo) {

        log.info("ArticleController_report_start: " + reportInfo.toString());

        boolean isReported = articleService.reportArticle(reportInfo);

        if (isReported) {   // 신고 성공하면 success
            log.info("ArticleController_report_end: success");
            return CommonResponse.success(SUCCESS);
        } else {    // 신고 실패하면 Exception
            throw new ArticlePossessionFailException();
        }
    }

    /**
     * 유저가 게시글의 상세 정보를 확인하기 위한 API
     *
     * @param articleId : 게시글의 Id
     */
    @GetMapping("/auth/{articleId}")
    public CommonResponse<?> find(@PathVariable Long articleId) {

        log.info("ArticleController_find_start: " + articleId);

        ArticleFindRes findInfo = articleService.findArticle(articleId);

        if (findInfo != null) { // 조회 성공하면 조회 결과 return
            log.info("ArticleController_find_end: " + findInfo.toString());
            return CommonResponse.success(findInfo);
        } else {    // 조회 실패하면 Exception
            throw new ArticleNotFoundException();
        }
    }

    /**
     * 유저가 게시글 목록을 조회하기 위한 API. keyword 를 공백으로 보내면 전체 검색
     *
     * @param keyword  : 검색어
     * @param pageable : Spring Data JPA의 페이징 기능
     * @return
     */
    @GetMapping("/auth")
    public CommonResponse<?> findAll(@RequestParam String keyword, Pageable pageable) {

        log.info("ArticleController_findAll_start: " + keyword + ", "
            + pageable.toString());

        Page<ArticleFindRes> findAllInfo = articleService.findAllArticle(keyword, pageable);

        if (findAllInfo != null) {  // 성공하면 조회 결과 리스트
            log.info("ArticleController_findAll_end: " + findAllInfo.toString());
            return CommonResponse.success(findAllInfo);
        } else {    // 실패하면 Exception
            throw new ArticleNotFoundException();
        }
    }
}
