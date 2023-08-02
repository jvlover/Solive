package com.ssafy.solive.api.service;

import com.ssafy.solive.api.request.ApplyDeletePutReq;
import com.ssafy.solive.api.request.ApplyFindGetReq;
import com.ssafy.solive.api.request.ApplyRegistPostReq;
import com.ssafy.solive.api.response.ApplyFindRes;
import java.util.List;

public interface ApplyService {

    void registApply(ApplyRegistPostReq registInfo);

    boolean deleteApply(ApplyDeletePutReq deleteInfo);

    List<ApplyFindRes> findByCondition(ApplyFindGetReq findCondition);
}
