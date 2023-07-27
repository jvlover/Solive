package com.ssafy.solive.api.service;

import com.ssafy.solive.api.request.ArticleDeletePutReq;
import com.ssafy.solive.api.request.ArticleLikePostReq;
import com.ssafy.solive.api.request.ArticleModifyPutReq;
import com.ssafy.solive.api.request.ArticleRegistPostReq;
import com.ssafy.solive.api.request.ArticleReportPostReq;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Transactional
@Service
public class ArticleServiceImpl implements ArticleService {

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

    @Override
    public Article registArticle(ArticleRegistPostReq registInfo, MultipartFile[] files) {
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

        String uploadFilePath = "C:/solive/image/";

        if (!Objects.isNull(files) && files[0].getSize() > 0) {
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
            }
        }
        return article;
    }

    // TODO: 게시판 사진 수정 사진 삭제 관련 로직 변경 필요
    @Override
    public boolean modifyArticle(ArticleModifyPutReq modifyInfo, MultipartFile[] files) {
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
            if (!Objects.isNull(files) && files[0].getSize() > 0) {
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
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteArticle(ArticleDeletePutReq deleteInfo) {
        Article article = articleRepository.findById(deleteInfo.getArticleId())
            .orElseThrow(IllegalArgumentException::new);
        // 현재 로그인 유저의 id와 글쓴이의 id가 일치할 때
        if (article.getUser().getId().equals(deleteInfo.getLoginUserId())) {
            // 게시글 삭제
            article.deleteArticle();
            // 게시글에 연관된 사진 리스트
            List<ArticlePicture> articlePictures = articlePictureRepository.findByArticle(
                article);
            // 해당 사진들 삭제
            for (ArticlePicture articlePicture : articlePictures) {
                articlePicture.deleteArticlePicture();
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean likeArticle(ArticleLikePostReq likeInfo) {
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

            return true;
        }
        return false;
    }

    @Override
    public boolean reportArticle(ArticleReportPostReq reportInfo) {
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

            return true;
        }
        return false;
    }
}
