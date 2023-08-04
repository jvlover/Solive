package com.ssafy.solive.api.matching.service;

import com.ssafy.solive.api.matching.request.QuestionDeletePutReq;
import com.ssafy.solive.api.matching.request.QuestionFindConditionGetReq;
import com.ssafy.solive.api.matching.request.QuestionModifyPutReq;
import com.ssafy.solive.api.matching.request.QuestionRegistPostReq;
import com.ssafy.solive.api.matching.response.QuestionFindConditionRes;
import com.ssafy.solive.api.matching.response.QuestionFindDetailRes;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface QuestionService {

    void registQuestion(QuestionRegistPostReq registInfo,
        List<MultipartFile> files);

    boolean deleteQuestion(QuestionDeletePutReq deleteInfo);

    boolean modifyQuestion(QuestionModifyPutReq modifyInfo);

    List<QuestionFindConditionRes> findByCondition(QuestionFindConditionGetReq findCondition);

    QuestionFindDetailRes findDetail(Long id);

}
