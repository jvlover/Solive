package com.ssafy.solive.api.service;

import com.ssafy.solive.api.request.QuestionDeletePutReq;
import com.ssafy.solive.api.request.QuestionRegistPostReq;
import com.ssafy.solive.db.entity.Question;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface QuestionService {

    Question registQuestion(QuestionRegistPostReq registInfo,
        List<MultipartFile> files);

    boolean deleteQuestion(QuestionDeletePutReq deleteInfo);
}
