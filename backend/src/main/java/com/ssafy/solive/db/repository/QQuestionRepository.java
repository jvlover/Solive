package com.ssafy.solive.db.repository;

import com.ssafy.solive.api.request.QuestionFindConditionGetReq;
import com.ssafy.solive.api.request.QuestionFindMineGetReq;
import com.ssafy.solive.api.response.QuestionFindConditionRes;
import com.ssafy.solive.api.response.QuestionFindDetailRes;
import com.ssafy.solive.api.response.QuestionFindMineRes;
import java.util.List;

/*
 *  Querydsl을 위한 Question Repository interface
 */
public interface QQuestionRepository {

    List<QuestionFindConditionRes> findByCondition(QuestionFindConditionGetReq findCondition);

    QuestionFindDetailRes findDetail(Long id);

    List<QuestionFindMineRes> findMyQuestion(
        QuestionFindMineGetReq findCondition);

    List<String> findQuestionImages(Long questionId);

    String findQuestionImage(Long questionId);
}
