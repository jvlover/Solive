package com.ssafy.solive.api.service;

import com.ssafy.solive.api.request.ArticleDeletePutReq;
import com.ssafy.solive.api.request.ArticleModifyPutReq;
import com.ssafy.solive.api.request.ArticleRegistPostReq;
import com.ssafy.solive.common.exception.FileUploadException;
import com.ssafy.solive.db.entity.Article;
import com.ssafy.solive.db.entity.ArticlePicture;
import com.ssafy.solive.db.entity.User;
import com.ssafy.solive.db.repository.ArticlePictureRepository;
import com.ssafy.solive.db.repository.ArticleRepository;
import com.ssafy.solive.db.repository.UserRepository;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Transactional
@Service
public class ArticleServiceImpl implements ArticleService {

    UserRepository userRepository;
    ArticleRepository articleRepository;
    ArticlePictureRepository articlePictureRepository;

    @Autowired
    public ArticleServiceImpl(UserRepository userRepository, ArticleRepository articleRepository,
        ArticlePictureRepository articlePictureRepository) {
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
        this.articlePictureRepository = articlePictureRepository;
    }

    @Override
    public Article registArticle(ArticleRegistPostReq registInfo, MultipartFile[] files) {
        // user가 null일 수 있으므로 controller로 exception throw
        // TODO: IllegalArgumentExeption 말고 custom exception 만들기
        User user = userRepository.findById(registInfo.getUserId())
            .orElseThrow(IllegalArgumentException::new);
        String title = registInfo.getTitle();
        String content = registInfo.getContent();

        Article article = Article.builder()
            .user(user)
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
                    throw new FileUploadException();
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
        if (article.getUser().getId().equals(modifyInfo.getLoginUserId())) {

            // 게시글 수정
            String title = modifyInfo.getTitle();
            String content = modifyInfo.getContent();
            article.modifyArticle(title, content);

            // 게시글 기존 사진 전부 삭제
            List<ArticlePicture> articlePictures = articlePictureRepository.findByArticle(article);
            articlePictureRepository.deleteAll(articlePictures);

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
                        throw new FileUploadException();
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
}
