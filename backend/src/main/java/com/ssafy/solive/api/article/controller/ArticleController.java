package com.ssafy.solive.api.article.controller;

import com.ssafy.solive.api.article.request.ArticleDeletePutReq;
import com.ssafy.solive.api.article.request.ArticleLikePostReq;
import com.ssafy.solive.api.article.request.ArticleModifyPutReq;
import com.ssafy.solive.api.article.request.ArticleRegistPostReq;
import com.ssafy.solive.api.article.request.ArticleReportPostReq;
import com.ssafy.solive.api.article.response.ArticleFindRes;
import com.ssafy.solive.api.article.service.ArticleService;
import com.ssafy.solive.common.exception.FileIOException;
import com.ssafy.solive.common.exception.article.ArticleNotFoundException;
import com.ssafy.solive.common.exception.article.ArticlePossessionFailException;
import com.ssafy.solive.common.model.CommonResponse;
import com.ssafy.solive.db.entity.Article;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/*
 *  유저가 게시판을 이용할 때 필요한 API를 모은 컨트롤러
 */

// TODO: CORS 문제 해결 필요
@Slf4j
@RestController
@RequestMapping("/board")
@CrossOrigin("*")
public class ArticleController {

    private static final String SUCCESS = "success"; // API 성공 시 return

    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    /*
     *  유저가 게시글을 등록하기 위한 API
     */
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public CommonResponse<?> regist(@RequestPart ArticleRegistPostReq registInfo,
        @RequestPart(value = "files", required = false) List<MultipartFile> fileList) {
        /*
         *  fileList : 게시글 사진, 게시글에는 사진이 반드시 있을 필요가 없음
         *  registInfo : 게시글 등록할 때 입력한 정보
         */
        if (fileList != null) { // 게시물에 파일 있으면
            log.info("ArticleController_regist_start: " + registInfo.toString() + ", "
                + fileList.toString());
        } else {
            log.info("ArticleController_regist_start: " + registInfo.toString());
        }

        Article article = articleService.registArticle(registInfo, fileList);
        if (article != null) {
            log.info("ArticleController_regist_end: success");
            return CommonResponse.success(SUCCESS);
        } else {
            log.info("ArticleController_regist_end: FileIOException");
            throw new FileIOException();
        }
    }

    /*
     *  유저가 게시글을 좋아요하기 위한 API
     */
    @PostMapping("/like")
    public CommonResponse<?> like(@RequestBody ArticleLikePostReq likeInfo) {
        /*
         *  likeInfo : 게시글 좋아요를 하기 위해 필요한 정보
         */
        log.info("ArticleController_like_start: " + likeInfo.toString());

        boolean isLiked = articleService.likeArticle(likeInfo);

        if (isLiked) {
            log.info("ArticleController_like_end: success");
            return CommonResponse.success(SUCCESS);
        } else {
            log.info("ArticleController_like_end: ArticlePossessionFailException");
            throw new ArticlePossessionFailException();
        }
    }

    /*
     *  유저가 게시글을 수정하기 위한 API
     */
    @PutMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public CommonResponse<?> modify(@RequestPart ArticleModifyPutReq modifyInfo,
        @RequestPart(value = "files", required = false) List<MultipartFile> fileList) {
        /*
         *  fileList : 게시글 사진, 게시글에는 사진이 반드시 있을 필요가 없음
         *  modifyInfo : 게시글 수정할 때 입력한 정보
         */
        if (fileList != null) {
            log.info("ArticleController_modify_start: " + modifyInfo.toString() + ", "
                + fileList.toString());
        } else {
            log.info("ArticleController_modify_start: " + modifyInfo.toString());
        }
        boolean isModified = articleService.modifyArticle(modifyInfo, fileList);

        if (isModified) {
            log.info("ArticleController_modify_end: success");
            return CommonResponse.success(SUCCESS);
        } else {
            log.info("ArticleController_modify_end: ArticlePossessionFailException");
            throw new ArticlePossessionFailException();
        }
    }

    /*
     *  유저가 게시글을 삭제하기 위한 API
     */
    @PutMapping("/delete")
    public CommonResponse<?> delete(@RequestBody ArticleDeletePutReq deleteInfo) {
        /*
         *  deleteInfo : 게시글을 삭제하기 위해 필요한 정보
         */
        log.info("ArticleController_delete_start: " + deleteInfo.toString());

        boolean isDeleted = articleService.deleteArticle(deleteInfo);

        if (isDeleted) {
            log.info("ArticleController_delete_end: success");
            return CommonResponse.success(SUCCESS);
        } else {
            log.info("ArticleController_delete_end: ArticlePossessionFailException");
            throw new ArticlePossessionFailException();
        }
    }

    /*
     *  유저가 게시글을 신고하기 위한 API
     */
    @PostMapping("/report")
    public CommonResponse<?> report(@RequestBody ArticleReportPostReq reportInfo) {
        /*
         *  reportInfo : 게시글을 신고하기 위해 필요한 정보
         */
        log.info("ArticleController_report_start: " + reportInfo.toString());

        boolean isReported = articleService.reportArticle(reportInfo);

        if (isReported) {
            log.info("ArticleController_report_end: success");
            return CommonResponse.success(SUCCESS);
        } else {
            log.info("ArticleController_report_end: ArticlePossessionFailException");
            throw new ArticlePossessionFailException();
        }
    }

    /*
     *  유저가 게시글의 상세 정보를 확인하기 위한 API
     */
    @GetMapping("/{articleId}")
    public CommonResponse<?> find(@PathVariable Long articleId) {
        /*
         *  articleId : 게시글의 Id
         */
        log.info("ArticleController_find_start: " + articleId);

        ArticleFindRes findInfo = articleService.findArticle(articleId);

        if (findInfo != null) {
            log.info("ArticleController_find_end: " + findInfo.toString());
            return CommonResponse.success(findInfo);
        } else {
            log.info("ArticleController_find_end: ArticleNotFoundException");
            throw new ArticleNotFoundException();
        }
    }

    /*
     *  유저가 게시글 목록을 조회하기 위한 API
     *  검색어, 시간 순 정렬 조건 선택 가능
     *  keyword를 공백으로 보내면 전체 검색
     */
    @GetMapping
    public CommonResponse<?> findAll(@RequestParam String keyword, Pageable pageable) {
        /*
         *  keyword : 검색어
         *  pageable : Spring Data JPA의 페이징 기능
         */
        log.info("ArticleController_findAll_start: " + keyword + ", "
            + pageable.toString());

        Page<ArticleFindRes> findAllInfo = articleService.findAllArticle(keyword, pageable);

        if (findAllInfo != null) {
            log.info("ArticleController_findAll_end: " + findAllInfo.toString());
            return CommonResponse.success(findAllInfo);
        } else {
            log.info("ArticleController_findAll_end: ArticleNotFoundException");
            throw new ArticleNotFoundException();
        }
    }
}
