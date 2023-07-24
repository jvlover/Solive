package com.ssafy.solive.api.service;

import com.ssafy.solive.api.request.QuestionRegistPostReq;
import com.ssafy.solive.db.entity.Question;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface QuestionService {

    public Question inputQuestion(QuestionRegistPostReq questionRegistPostReq,
        List<MultipartFile> files);
}
