package com.ssafy.solive.db.repository;

import com.ssafy.solive.db.entity.ArticleReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleReportRepository extends JpaRepository<ArticleReport, Long> {

}
