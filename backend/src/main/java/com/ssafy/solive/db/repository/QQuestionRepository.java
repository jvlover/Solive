package com.ssafy.solive.db.repository;

import com.ssafy.solive.api.request.QuestionFindConditionGetReq;
import com.ssafy.solive.api.response.QuestionFindConditionRes;
import com.ssafy.solive.api.response.QuestionFindDetailRes;
import java.util.List;

/*
 *  Querydsl을 위한 Repository interface
 */
public interface QQuestionRepository {

    List<QuestionFindConditionRes> findByCondition(QuestionFindConditionGetReq findCondition);

    QuestionFindDetailRes findDetail(Long id);
}
