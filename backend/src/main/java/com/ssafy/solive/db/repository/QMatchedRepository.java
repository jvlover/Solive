package com.ssafy.solive.db.repository;

import com.ssafy.solive.api.request.MatchedFindMineGetReq;
import com.ssafy.solive.api.response.MatchedFindMineRes;
import java.util.List;

/*
 *  Querydsl을 위한 Matched Repository interface
 */
public interface QMatchedRepository {

    List<MatchedFindMineRes> findMyMatching(MatchedFindMineGetReq findCondition);
}
