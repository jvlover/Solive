package com.ssafy.solive.db.repository;

import com.ssafy.solive.db.entity.Apply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplyRepository extends JpaRepository<Apply, Long>, QApplyRepository {

}
