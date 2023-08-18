package com.ssafy.solive.api.matching.service;

import com.ssafy.solive.api.matching.request.ApplyDeletePutReq;
import com.ssafy.solive.api.matching.request.ApplyFindGetReq;
import com.ssafy.solive.api.matching.request.ApplyRegistPostReq;
import com.ssafy.solive.api.matching.response.ApplyFindRes;
import com.ssafy.solive.api.matching.response.ApplyRegistPostRes;
import java.util.List;

public interface ApplyService {

    ApplyRegistPostRes registApply(ApplyRegistPostReq registInfo);

    boolean deleteApply(ApplyDeletePutReq deleteInfo);

    List<ApplyFindRes> findByCondition(ApplyFindGetReq findCondition);
}
