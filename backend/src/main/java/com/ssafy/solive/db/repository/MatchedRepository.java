package com.ssafy.solive.db.repository;

import com.ssafy.solive.db.entity.Matched;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchedRepository extends JpaRepository<Matched, Long> {

}
