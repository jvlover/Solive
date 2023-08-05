package com.ssafy.solive.db.repository;

import com.ssafy.solive.db.entity.Favorite;
import com.ssafy.solive.db.entity.FavoriteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, FavoriteId> {

}
