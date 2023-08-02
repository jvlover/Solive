package com.ssafy.solive.db.repository;

import com.ssafy.solive.api.request.ApplyFindGetReq;
import com.ssafy.solive.api.response.ApplyFindRes;
import java.util.List;

/*
 *  Querydsl을 위한 Apply Repository interface
 */
public interface QApplyRepository {

    List<ApplyFindRes> findByCondition(ApplyFindGetReq findCondition);
}
