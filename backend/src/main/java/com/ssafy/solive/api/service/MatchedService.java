package com.ssafy.solive.api.service;

import com.ssafy.solive.api.request.MatchedFindMineGetReq;
import com.ssafy.solive.api.request.MatchedRegistPostReq;
import com.ssafy.solive.api.response.MatchedFindMineRes;
import java.util.List;

public interface MatchedService {

    void registMatched(MatchedRegistPostReq registInfo);

    List<MatchedFindMineRes> findMyMatching(MatchedFindMineGetReq findCondition);

}
