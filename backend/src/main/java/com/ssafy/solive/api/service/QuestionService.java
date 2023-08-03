package com.ssafy.solive.api.service;

import com.ssafy.solive.api.request.QuestionDeletePutReq;
import com.ssafy.solive.api.request.QuestionFindConditionGetReq;
import com.ssafy.solive.api.request.QuestionModifyPutReq;
import com.ssafy.solive.api.request.QuestionRegistPostReq;
import com.ssafy.solive.api.response.QuestionFindConditionRes;
import com.ssafy.solive.api.response.QuestionFindDetailRes;
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
