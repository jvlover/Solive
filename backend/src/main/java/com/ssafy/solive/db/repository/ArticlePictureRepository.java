package com.ssafy.solive.db.repository;

import com.ssafy.solive.db.entity.ArticlePicture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticlePictureRepository extends JpaRepository<ArticlePicture, Long> {

}
