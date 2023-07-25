package com.ssafy.solive.db.repository;

import com.ssafy.solive.db.entity.Article;
import com.ssafy.solive.db.entity.ArticlePicture;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticlePictureRepository extends JpaRepository<ArticlePicture, Long> {

    // 게시글에 있는 사진들을 해당 게시글로 찾는 메소드
    List<ArticlePicture> findByArticle(Article article);
}
