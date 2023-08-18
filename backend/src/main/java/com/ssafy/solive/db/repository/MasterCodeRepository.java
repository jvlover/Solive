package com.ssafy.solive.db.repository;

import com.ssafy.solive.db.entity.MasterCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MasterCodeRepository extends JpaRepository<MasterCode, Integer> {

    MasterCode findByName(String name);
}
