package com.ssafy.solive.api.service;

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
import java.util.Objects;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
}
