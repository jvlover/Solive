package com.ssafy.solive.api.matching.service;

import com.ssafy.solive.api.matching.request.MatchedFindMineGetReq;
import com.ssafy.solive.api.matching.request.MatchedRegistPostReq;
import com.ssafy.solive.api.matching.response.MatchedFindMineRes;
import com.ssafy.solive.db.entity.User;
import java.util.List;

public interface MatchedService {

    User registMatched(MatchedRegistPostReq registInfo);

    List<MatchedFindMineRes> findMyMatching(MatchedFindMineGetReq findCondition);

}
