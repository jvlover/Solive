package com.ssafy.solive.api.matching.service;

import com.ssafy.solive.api.matching.request.QuestionDeletePutReq;
import com.ssafy.solive.api.matching.request.QuestionFindConditionGetReq;
import com.ssafy.solive.api.matching.request.QuestionModifyPutReq;
import com.ssafy.solive.api.matching.request.QuestionRegistPostReq;
import com.ssafy.solive.api.matching.response.QuestionFindDetailRes;
import com.ssafy.solive.api.matching.response.QuestionFindRes;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface QuestionService {

    void registQuestion(QuestionRegistPostReq registInfo,
        List<MultipartFile> files);

    boolean deleteQuestion(QuestionDeletePutReq deleteInfo);

    boolean modifyQuestion(QuestionModifyPutReq modifyInfo);

    List<QuestionFindRes> findByCondition(QuestionFindConditionGetReq findCondition);

    QuestionFindDetailRes findDetail(Long id);

    List<QuestionFindRes> findLatestQuestionForTeacher();

    List<QuestionFindRes> findFavoriteQuestionForTeacher(Long userId);

}
