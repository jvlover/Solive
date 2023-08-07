package com.ssafy.solive.db.repository;

import com.ssafy.solive.db.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long>, QQuestionRepository {

}
