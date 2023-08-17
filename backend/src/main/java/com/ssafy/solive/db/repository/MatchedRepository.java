package com.ssafy.solive.db.repository;

import com.ssafy.solive.db.entity.Matched;
import com.ssafy.solive.db.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchedRepository extends JpaRepository<Matched, Long>, QMatchedRepository {

    Matched findBySessionId(String sessionId);

    Matched findByQuestion(Question question);
}
