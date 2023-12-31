package com.ssafy.solive.api.matching.service;

import com.ssafy.solive.api.matching.request.MatchedFindMineGetReq;
import com.ssafy.solive.api.matching.request.MatchedPutReq;
import com.ssafy.solive.api.matching.request.MatchedRegistPostReq;
import com.ssafy.solive.api.matching.response.MatchedFindMineRes;
import com.ssafy.solive.api.matching.response.MatchedFindVideoUrlRes;
import com.ssafy.solive.api.matching.response.MatchedRegistPostRes;
import java.util.List;

public interface MatchedService {

    MatchedRegistPostRes registMatched(MatchedRegistPostReq registInfo);

    List<MatchedFindMineRes> findMyMatching(MatchedFindMineGetReq findCondition);

    void startMatching(MatchedPutReq sessionInfo);

    void extendMatching(MatchedPutReq sessionInfo);

    void endMatching(MatchedPutReq sessionInfo);

    MatchedFindVideoUrlRes findVideoUrl(Long questionId);
}
