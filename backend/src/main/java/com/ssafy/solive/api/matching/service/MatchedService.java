package com.ssafy.solive.api.matching.service;

import com.ssafy.solive.api.matching.request.MatchedFindMineGetReq;
import com.ssafy.solive.api.matching.request.MatchedRegistPostReq;
import com.ssafy.solive.api.matching.response.MatchedFindMineRes;
import com.ssafy.solive.api.matching.response.MatchedRegistPostRes;
import java.util.List;

public interface MatchedService {

    MatchedRegistPostRes registMatched(MatchedRegistPostReq registInfo);

    List<MatchedFindMineRes> findMyMatching(MatchedFindMineGetReq findCondition);

}
