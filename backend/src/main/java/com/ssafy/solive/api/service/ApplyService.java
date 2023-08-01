package com.ssafy.solive.api.service;

import com.ssafy.solive.api.request.ApplyDeletePutReq;
import com.ssafy.solive.api.request.ApplyRegistPostReq;

public interface ApplyService {

    void registApply(ApplyRegistPostReq registInfo);

    boolean deleteApply(ApplyDeletePutReq deleteInfo);
}
