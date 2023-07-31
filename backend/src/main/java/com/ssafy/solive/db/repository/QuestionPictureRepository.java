package com.ssafy.solive.db.repository;

import com.ssafy.solive.db.entity.QuestionPicture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionPictureRepository extends JpaRepository<QuestionPicture, Long> {

}
