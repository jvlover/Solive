package com.ssafy.solive.db.repository;

import com.ssafy.solive.db.entity.ArticleLike;
import com.ssafy.solive.db.entity.ArticleLikeId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleLikeRepsitory extends JpaRepository<ArticleLike, ArticleLikeId> {

}
