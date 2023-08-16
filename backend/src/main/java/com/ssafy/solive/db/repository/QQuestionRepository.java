package com.ssafy.solive.db.repository;

import com.ssafy.solive.api.matching.request.MatchedFindMineGetReq;
import com.ssafy.solive.api.matching.request.QuestionFindConditionGetReq;
import com.ssafy.solive.api.matching.response.MatchedFindMineRes;
import com.ssafy.solive.api.matching.response.QuestionFindDetailRes;
import com.ssafy.solive.api.matching.response.QuestionFindRes;
import java.util.List;

/**
 * Querydsl을 위한 Question Repository interface
 */
public interface QQuestionRepository {

    List<QuestionFindRes> findByCondition(QuestionFindConditionGetReq findCondition);

    QuestionFindDetailRes findDetail(Long id);

    List<MatchedFindMineRes> findMyQuestion(MatchedFindMineGetReq findCondition);

    List<String> findQuestionImages(Long questionId);

    String findQuestionImage(Long questionId);

    List<QuestionFindRes> findLatestQuestionForTeacher();
}
