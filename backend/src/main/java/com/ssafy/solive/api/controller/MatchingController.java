package com.ssafy.solive.api.controller;

import com.ssafy.solive.api.request.QuestionDeletePutReq;
import com.ssafy.solive.api.request.QuestionModifyPutReq;
import com.ssafy.solive.api.request.QuestionRegistPostReq;
import com.ssafy.solive.api.service.MatchingService;
import com.ssafy.solive.common.exception.QuestionDeleteFailException;
import com.ssafy.solive.common.exception.QuestionModifyFailException;
import com.ssafy.solive.common.model.CommonResponse;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/matching")
public class MatchingController {

    private static final String SUCCESS = "success";

    MatchingService matchingService;

    @Autowired
    public MatchingController(MatchingService matchingService) {
        this.matchingService = matchingService;
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public CommonResponse<?> regist(@RequestPart QuestionRegistPostReq registInfo,
        @RequestPart("files") List<MultipartFile> files) {
        // TODO: 인증 된 사용자인지 확인하는 과정 필요
        matchingService.registQuestion(registInfo, files);
        return CommonResponse.success(SUCCESS);
    }

    @PutMapping("/delete")
    public CommonResponse<?> delete(@RequestBody QuestionDeletePutReq deleteInfo) {
        // TODO: 인증 된 사용자인지 확인하는 과정 필요
        // TODO: is_matched 가 true 인 경우 삭제할 수 있게 할지, 없게 할지 체크하는 과정 고려
        boolean isDeleted = matchingService.deleteQuestion(deleteInfo);
        if (isDeleted) {
            return CommonResponse.success(SUCCESS);
        } else {
            throw new QuestionDeleteFailException();
        }
    }

    @PutMapping()
    public CommonResponse<?> modify(@RequestBody QuestionModifyPutReq modifyInfo) {
        // TODO: 인증 된 사용자인지 확인하는 과정 필요
        // TODO: is_matched가 true인 경우 수정 가능하게 할지 안 하게 할지
        boolean isModified = matchingService.modifyQuestion(modifyInfo);
        if (isModified) {
            return CommonResponse.success(SUCCESS);
        } else {
            throw new QuestionModifyFailException();
        }
    }
}
