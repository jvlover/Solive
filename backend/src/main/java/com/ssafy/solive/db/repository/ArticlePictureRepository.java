package com.ssafy.solive.db.repository;

import com.ssafy.solive.db.entity.Article;
import com.ssafy.solive.db.entity.ArticlePicture;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticlePictureRepository extends JpaRepository<ArticlePicture, Long> {

    // 게시글에 있는 사진들을 해당 게시글로 찾는 메소드
    List<ArticlePicture> findByArticle(Article article);

    // 게시글 id로 게시글 사진의 경로를 찾는 JPQL 메소드
    @Query("select ap.pathName from ArticlePicture ap join ap.article a where a.id = :articleId")
    List<String> findPathNameByArticle(@Param("articleId") Long articleId);

    // 게시글에 사진이 존재하는지 찾는 메소드
    boolean existsByArticle(Article article);
}
