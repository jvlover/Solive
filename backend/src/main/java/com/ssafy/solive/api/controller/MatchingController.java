package com.ssafy.solive.api.controller;

import com.ssafy.solive.api.request.QuestionRegistPostReq;
import com.ssafy.solive.api.service.QuestionService;
import com.ssafy.solive.common.model.CommonResponse;
import com.ssafy.solive.db.entity.Question;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/matching")
public class MatchingController {

    QuestionService questionService;

    @Autowired
    public MatchingController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public CommonResponse<?> inputQuestion(@RequestPart QuestionRegistPostReq questionRegistPostReq,
        @RequestPart("files") List<MultipartFile> files) {
        Question question = questionService.inputQuestion(questionRegistPostReq, files);
        return CommonResponse.success("success");
    }
}
