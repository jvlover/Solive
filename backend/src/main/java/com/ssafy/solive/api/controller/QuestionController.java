package com.ssafy.solive.api.controller;

import com.ssafy.solive.api.request.QuestionDeletePutReq;
import com.ssafy.solive.api.request.QuestionFindConditionGetReq;
import com.ssafy.solive.api.request.QuestionFindMineGetReq;
import com.ssafy.solive.api.request.QuestionModifyPutReq;
import com.ssafy.solive.api.request.QuestionRegistPostReq;
import com.ssafy.solive.api.response.QuestionFindConditionRes;
import com.ssafy.solive.api.response.QuestionFindDetailRes;
import com.ssafy.solive.api.response.QuestionFindMineRes;
import com.ssafy.solive.api.service.QuestionService;
import com.ssafy.solive.common.exception.QuestionPossessionFailException;
import com.ssafy.solive.common.model.CommonResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/*
 *  학생과 강사가 문제를 Matching 할 때 필요한 API를 모은 컨트롤러
 */

@Slf4j
@RestController
@RequestMapping("/question")
public class QuestionController {

    private static final String SUCCESS = "success";  // API 성공 시 return

    QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    /*
     *  유저(학생)가 문제를 등록하기 위한 API
     */
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public CommonResponse<?> regist(@RequestPart QuestionRegistPostReq registInfo,
        @RequestPart("files") List<MultipartFile> files) {
        /*
         *  files : 문제 사진. 문제는 반드시 사진이 하나 이상 있어야 하므로 null일 수 없음
         *  registInfo : 문제 등록할 때 입력한 정보
         */
        // TODO: 인증 된 사용자인지 확인하는 과정 필요

        log.info("QuestionController_regist_start: " + registInfo.toString() + ", "
            + files.toString());

        questionService.registQuestion(registInfo, files);

        log.info("QuestionController_regist_end: success");
        return CommonResponse.success(SUCCESS);
    }

    /*
     *  유저(학생)이 문제를 삭제하기 위한 API
     */
    @PutMapping("/delete")
    public CommonResponse<?> delete(@RequestBody QuestionDeletePutReq deleteInfo) {
        /*
         *  deleteInfo : 문제 삭제하기 위해 필요한 정보
         */
        // TODO: 인증 된 사용자인지 확인하는 과정 필요

        log.info("QuestionController_delete_start: " + deleteInfo.toString());

        boolean isDeleted = questionService.deleteQuestion(deleteInfo); // 삭제 실패하면 false
        if (isDeleted) {
            log.info("QuestionController_delete_end: success");
            return CommonResponse.success(SUCCESS);
        } else {
            // 유저가 작성하지 않은 문제를 삭제하려고 하는 경우
            log.info("QuestionController_delete_end: QuestionPossessionFailException");
            throw new QuestionPossessionFailException();
        }
    }

    /*
     *  유저(학생)이 문제를 수정하기 위한 API
     */
    @PutMapping()
    public CommonResponse<?> modify(@RequestBody QuestionModifyPutReq modifyInfo) {
        /*
         *  modifyInfo : 문제 수정하기 위해 필요한 정보
         */
        // TODO: 인증 된 사용자인지 확인하는 과정 필요

        log.info("QuestionController_modify_start: " + modifyInfo.toString());

        boolean isModified = questionService.modifyQuestion(modifyInfo); // 수정 실패하면 false
        if (isModified) {
            log.info("QuestionController_modify_end: success");
            return CommonResponse.success(SUCCESS);
        } else {
            log.info("QuestionController_modify_end: QuestionPossessionFailException");
            throw new QuestionPossessionFailException();
        }
    }

    /*
     *  유저(강사)가 문제를 검색하기 위한 API
     *  제목 검색어, 과목 코드, 시간 순 정렬 조건 선택 가능
     */
    @GetMapping()
    public CommonResponse<?> findByCondition(QuestionFindConditionGetReq findCondition) {
        /*
         *  findCondition : 검색 조건
         */

        log.info("QuestionController_findByCondition_start: " + findCondition.toString());

        List<QuestionFindConditionRes> findResList = questionService.findByCondition(findCondition);

        log.info("QuestionController_findByCondition_end: " + findResList.toString());
        return CommonResponse.success(findResList);
    }

    /*
     *  유저가 문제의 상세 정보를 확인하기 위한 API
     */
    @GetMapping("/{id}")
    public CommonResponse<?> findDetail(@PathVariable Long id, HttpServletRequest req) {
        /*
         *  id : question의 id
         *  req : jwt token check 할 때 필요할 것 같아서 일단 넣었는데 아직 모름
         */

        log.info("QuestionController_findDetail_start: " + id + ", " + req.toString());

        QuestionFindDetailRes findDetailRes = questionService.findDetail(id);

        log.info("QuestionController_findDetail_end: " + findDetailRes.toString());
        return CommonResponse.success(findDetailRes);
    }

    /*
     *  유저(학생)가 자신이 등록했던 문제를 검색하기 위한 API
     *  매칭 상태, 제목 검색어, 과목 코드, 시간 순 정렬 조건 선택 가능
     */
    @GetMapping("/my")
    public CommonResponse<?> findMyQuestion(QuestionFindMineGetReq findCondition) {
        /*
         *  findCondition : 검색 조건
         */

        log.info("QuestionController_findMyQuestion_start: " + findCondition.toString());

        List<QuestionFindMineRes> findResList = questionService.findMyQuestion(findCondition);

        log.info("QuestionController_findMyQuestion_end: " + findResList.toString());
        return CommonResponse.success(findResList);
    }

}
