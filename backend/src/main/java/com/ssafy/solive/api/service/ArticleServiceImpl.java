package com.ssafy.solive.api.service;

import com.ssafy.solive.api.request.ArticleDeletePutReq;
import com.ssafy.solive.api.request.ArticleLikePostReq;
import com.ssafy.solive.api.request.ArticleModifyPutReq;
import com.ssafy.solive.api.request.ArticleRegistPostReq;
import com.ssafy.solive.api.request.ArticleReportPostReq;
import com.ssafy.solive.api.response.ArticleFindAllRes;
import com.ssafy.solive.api.response.ArticleFindRes;
import com.ssafy.solive.common.exception.FileIOException;
import com.ssafy.solive.db.entity.Article;
import com.ssafy.solive.db.entity.ArticleLike;
import com.ssafy.solive.db.entity.ArticleLikeId;
import com.ssafy.solive.db.entity.ArticlePicture;
import com.ssafy.solive.db.entity.ArticleReport;
import com.ssafy.solive.db.entity.ArticleReportId;
import com.ssafy.solive.db.entity.MasterCode;
import com.ssafy.solive.db.entity.User;
import com.ssafy.solive.db.repository.ArticleLikeRepsitory;
import com.ssafy.solive.db.repository.ArticlePictureRepository;
import com.ssafy.solive.db.repository.ArticleReportRepository;
import com.ssafy.solive.db.repository.ArticleRepository;
import com.ssafy.solive.db.repository.MasterCodeRepository;
import com.ssafy.solive.db.repository.UserRepository;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/*
 *  유저가 게시판을 이용할 때의 API에 대한 서비스
 */

@Slf4j
@Transactional
@Service
public class ArticleServiceImpl implements ArticleService {

    // Spring Data Jpa 사용을 위한 Repository들
    UserRepository userRepository;
    MasterCodeRepository masterCodeRepository;
    ArticleRepository articleRepository;
    ArticlePictureRepository articlePictureRepository;
    ArticleLikeRepsitory articleLikeRepsitory;
    ArticleReportRepository articleReportRepository;

    @Autowired
    public ArticleServiceImpl(UserRepository userRepository,
        MasterCodeRepository masterCodeRepository,
        ArticleRepository articleRepository, ArticlePictureRepository articlePictureRepository,
        ArticleLikeRepsitory articleLikeRepsitory,
        ArticleReportRepository articleReportRepository) {
        this.userRepository = userRepository;
        this.masterCodeRepository = masterCodeRepository;
        this.articleRepository = articleRepository;
        this.articlePictureRepository = articlePictureRepository;
        this.articleLikeRepsitory = articleLikeRepsitory;
        this.articleReportRepository = articleReportRepository;
    }

    /*
     *  Regist API에 대한 서비스
     */
    @Override
    public Article registArticle(ArticleRegistPostReq registInfo,
        List<MultipartFile> files) {
        /*
         *  files : 게시글 사진, 게시글에는 사진이 반드시 있을 필요가 없음
         *  registInfo : 게시글 등록할 때 입력한 정보
         */
        log.info("ArticleService_registArticle_start: " + registInfo.toString() + ", "
            + files.toString());

        // user가 null일 수 있으므로 controller로 exception throw
        // TODO: IllegalArgumentExeption 말고 custom exception 만들기
        User user = userRepository.findById(registInfo.getUserId())
            .orElseThrow(IllegalArgumentException::new);
        MasterCode masterCode = masterCodeRepository.findById(registInfo.getMasterCodeId())
            .orElseThrow(IllegalArgumentException::new);
        String title = registInfo.getTitle();
        String content = registInfo.getContent();

        Article article = Article.builder()
            .user(user)
            .masterCode(masterCode)
            .title(title)
            .content(content)
            .build();

        articleRepository.save(article);

        /*
         *  files를 바탕으로 ArticlePicture Entity 생성 시작
         */

        // 파일 업로드 경로
        String uploadFilePath = "C:/solive/image/";

        if (!Objects.isNull(files) && files.get(0).getSize() > 0) {
            for (MultipartFile file : files) {
                String originalFileName = file.getOriginalFilename();

                // 파일 확장자 명
                String suffix = originalFileName
                    .substring(originalFileName.lastIndexOf(".") + 1,
                        originalFileName.length());

                // 랜덤한 파일 이름 생성
                String fileName = UUID.randomUUID().toString() + "." + suffix;

                // 파일 업로드 경로 디렉토리가 만약 존재하지 않으면 생성
                File folder = new File(uploadFilePath);
                if (!folder.isDirectory()) {
                    folder.mkdirs();
                }

                String pathName = uploadFilePath + fileName; // 파일 절대 경로
                String resourcePathName = "/image/" + fileName; // url

                File dest = new File(pathName);

                try {
                    file.transferTo(dest);

                    String contentType = file.getContentType();
                    int size = (int) file.getSize();

                    ArticlePicture articlePicture = ArticlePicture.builder()
                        .article(article)
                        .contentType(contentType)
                        .imageName(originalFileName)
                        .fileName(fileName)
                        .pathName(pathName)
                        .size(size)
                        .url(resourcePathName)
                        .build();

                    articlePictureRepository.save(articlePicture);

                } catch (IOException e) {
                    throw new FileIOException();
                }
                /*
                 *  생성한 ArticlePicture Entity를 DB에 insert 완료
                 */
            }
        }

        log.info("ArticleService_registArticle_end: success");
        return article;
    }

    /*
     *  modify API에 대한 서비스
     */
    // TODO: 게시판 사진 수정 사진 삭제 관련 로직 변경 필요
    @Override
    public boolean modifyArticle(ArticleModifyPutReq modifyInfo, List<MultipartFile> files) {
        /*
         *  files : 게시글 사진, 게시글에는 사진이 반드시 있을 필요가 없음
         *  modifyInfo : 게시글 수정할 때 입력한 정보
         */
        log.info("ArticleService_modifyArticle_start: " + modifyInfo.toString() + ", "
            + files.toString());

        Article article = articleRepository.findById(modifyInfo.getArticleId())
            .orElseThrow(IllegalArgumentException::new);
        // 현재 로그인 유저의 id와 글쓴이의 id가 일치할 때
        if (article.getUser().getId().equals(modifyInfo.getUserId())) {

            // 게시글 수정
            MasterCode masterCode = masterCodeRepository.findById(modifyInfo.getMasterCodeId())
                .orElseThrow(IllegalArgumentException::new);
            String title = modifyInfo.getTitle();
            String content = modifyInfo.getContent();
            article.modifyArticle(masterCode, title, content);

            // 게시글 기존 사진 전부 삭제
            List<ArticlePicture> articlePictures = articlePictureRepository.findByArticle(article);
            // 실제 사진 삭제 및 DB에서도 삭제
            for (ArticlePicture articlePicture : articlePictures) {
                try {
                    Path deleteFilePath = Paths.get(articlePicture.getPathName());
                    Files.deleteIfExists(deleteFilePath);
                    articlePictureRepository.delete(articlePicture);
                } catch (IOException e) {
                    throw new FileIOException();
                }
            }

            // 게시글 사진 다시 업로드
            String uploadFilePath = "C:/solive/image/";
            if (!Objects.isNull(files) && files.get(0).getSize() > 0) {
                for (MultipartFile file : files) {
                    String originalFileName = file.getOriginalFilename();

                    String prefix = originalFileName
                        .substring(originalFileName.lastIndexOf(".") + 1,
                            originalFileName.length());

                    String fileName = UUID.randomUUID().toString() + "." + prefix;

                    File folder = new File(uploadFilePath);

                    if (!folder.isDirectory()) {
                        folder.mkdirs();
                    }

                    String pathName = uploadFilePath + fileName;
                    String resourcePathName = "/image/" + fileName;

                    File dest = new File(pathName);

                    try {
                        file.transferTo(dest);

                        String contentType = file.getContentType();
                        int size = (int) file.getSize();

                        ArticlePicture articlePicture = ArticlePicture.builder()
                            .article(article)
                            .contentType(contentType)
                            .imageName(originalFileName)
                            .fileName(fileName)
                            .pathName(pathName)
                            .size(size)
                            .url(resourcePathName)
                            .build();

                        articlePictureRepository.save(articlePicture);

                    } catch (IOException e) {
                        throw new FileIOException();
                    }
                    /*
                     *  생성한 ArticlePicture Entity를 DB에 insert 완료
                     */
                }
            }
            log.info("ArticleService_modifyArticle_end: true");
            return true;
        }
        log.info("ArticleService_modifyArticle_end: false");
        return false;
    }

    /*
     *  delete API에 대한 서비스
     */
    @Override
    public boolean deleteArticle(ArticleDeletePutReq deleteInfo) {
        /*
         *  deleteInfo : 게시글을 삭제하기 위해 필요한 정보
         */
        log.info("ArticleService_deleteArticle_start: " + deleteInfo.toString());

        Article article = articleRepository.findById(deleteInfo.getArticleId())
            .orElseThrow(IllegalArgumentException::new);
        // deleteInfo의 유저 정보와 해당 문제의 실제 유저 정보가 같아야만 삭제
        if (article.getUser().getId().equals(deleteInfo.getUserId())) {
            // 게시글 삭제
            article.deleteArticle();
            // 게시글에 연관된 사진 리스트
            List<ArticlePicture> articlePictures = articlePictureRepository.findByArticle(
                article);
            // 해당 사진들 삭제
            for (ArticlePicture articlePicture : articlePictures) {
                articlePicture.deleteArticlePicture();
            }
            log.info("ArticleService_deleteArticle_end: true");
            return true;
        }
        // deleteInfo의 유저 정보와 해당 문제의 실제 유저 정보가 다를 경우
        log.info("ArticleService_deleteArticle_end: false");
        return false;
    }

    /*
     *  like API에 대한 서비스
     */
    @Override
    public boolean likeArticle(ArticleLikePostReq likeInfo) {
        /*
         *  likeInfo : 게시글을 좋아요하기 위해 필요한 정보
         */
        log.info("ArticleService_likeArticle_start: " + likeInfo.toString());

        ArticleLikeId articleLikeId = ArticleLikeId.builder()
            .user(likeInfo.getUserId())
            .article(likeInfo.getArticleId())
            .build();

        Optional<ArticleLike> optionalArticleLike = articleLikeRepsitory.findById(articleLikeId);

        if (optionalArticleLike.isEmpty()) {
            User user = userRepository.findById(likeInfo.getUserId())
                .orElseThrow(IllegalArgumentException::new);
            Article article = articleRepository.findById(likeInfo.getArticleId())
                .orElseThrow(IllegalArgumentException::new);

            ArticleLike articleLike = ArticleLike.builder()
                .user(user)
                .article(article)
                .build();

            articleLikeRepsitory.save(articleLike);

            article.likeArticle();

            log.info("ArticleService_likeArticle_end: true");
            return true;
        }
        log.info("ArticleService_likeArticle_end: false");
        return false;
    }

    /*
     *  report API에 대한 서비스
     */
    @Override
    public boolean reportArticle(ArticleReportPostReq reportInfo) {
        /*
         *  reportInfo : 게시글을 신고하기 위해 필요한 정보
         */
        log.info("ArticleService_reportArticle_start: " + reportInfo.toString());

        ArticleReportId articleReportId = ArticleReportId.builder()
            .userReportId(reportInfo.getUserReportId())
            .article(reportInfo.getArticleId())
            .build();

        Optional<ArticleReport> optionalArticleReport = articleReportRepository.findById(
            articleReportId);

        if (optionalArticleReport.isEmpty()) {
            User reportUser = userRepository.findById(reportInfo.getUserReportId())
                .orElseThrow(IllegalArgumentException::new);
            User reportedUser = userRepository.findById(reportInfo.getUserReportedId())
                .orElseThrow(IllegalArgumentException::new);
            Article article = articleRepository.findById(reportInfo.getArticleId())
                .orElseThrow(IllegalArgumentException::new);
            String content = reportInfo.getContent();

            ArticleReport articleReport = ArticleReport.builder()
                .userReportId(reportUser)
                .userReportedId(reportedUser)
                .article(article)
                .content(content)
                .build();

            articleReportRepository.save(articleReport);

            article.reportArticle();

            // 신고 5회 이상 누적시 글 삭제
            if (article.getReportCount() >= 5) {
                article.deleteArticle();
                List<ArticlePicture> articlePictures = articlePictureRepository.findByArticle(
                    article);
                for (ArticlePicture articlePicture : articlePictures) {
                    articlePicture.deleteArticlePicture();
                }
            }
            log.info("ArticleService_reportArticle_end: true");
            return true;
        }
        log.info("ArticleService_reportArticle_end: false");
        return false;
    }

    /*
     *  유저가 게시글의 상세 정보를 확인하기 위한 API 서비스
     */
    @Override
    public ArticleFindRes findArticle(Long articleId) {
        /*
         *  articleId : 게시글의 Id
         */
        log.info("ArticleService_findArticle_start: " + articleId);

        Article article = articleRepository.findById(articleId)
            .orElseThrow(IllegalArgumentException::new);
        List<String> articlePicturePathNames = articlePictureRepository.findPathNameByArticle(
            articleId);

        ArticleFindRes articleFindRes = ArticleFindRes.builder()
            .masterCodeId(article.getMasterCode().getId())
            .userId(article.getUser().getId())
            .title(article.getTitle())
            .content(article.getContent())
            .viewCount(article.getViewCount())
            .likeCount(article.getLikeCount())
            .reportCount(article.getReportCount())
            .time(article.getTime().toString())
            .lastUpdateTime(article.getLastUpdateTime().toString())
            .articlePicturePathNames(articlePicturePathNames)
            .build();

        log.info("ArticleService_findArticle_end: " + articleFindRes.toString());
        return articleFindRes;
    }

    /*
     *  유저가 게시글 목록을 조회하기 위한 API 서비스
     *  검색어, 시간 순 정렬 조건 선택 가능
     *  keyword를 공백으로 보내면 전체 검색
     */
    @Override
    public Page<ArticleFindAllRes> findAllArticle(String keyword, Pageable pageable) {
        /*
         *  keyword : 검색어
         *  pageable : Spring Data JPA의 페이징 기능
         */
        log.info("ArticleService_findAllArticle_start: " + keyword + ", "
            + pageable.toString());

        Page<ArticleFindAllRes> articleFindAllRes = articleRepository.findByTitleContaining(keyword,
                pageable)
            .map(m -> ArticleFindAllRes.builder()
                .userId(m.getUser().getId())
                .title(m.getTitle())
                .viewCount(m.getViewCount())
                .likeCount(m.getLikeCount())
                .time(m.getTime().toString())
                .hasPicture(articlePictureRepository.existsByArticle(m))
                .build());

        log.info("ArticleService_findAllArticle_end: " + articleFindAllRes.toString());

        return articleFindAllRes;
    }
}
