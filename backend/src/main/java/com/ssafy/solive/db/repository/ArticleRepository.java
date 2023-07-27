package com.ssafy.solive.db.repository;

import com.ssafy.solive.db.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    // 파라미터로 받은 키워드를 포함(LIKE와 같음)하는 제목을 가진 글들을 반환 (페이지네이션)
    Page<Article> findByTitleContaining(String keyword, Pageable pageable);
}
