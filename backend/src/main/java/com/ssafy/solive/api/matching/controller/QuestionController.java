package com.ssafy.solive.api.matching.controller;

import com.ssafy.solive.api.matching.request.QuestionDeletePutReq;
import com.ssafy.solive.api.matching.request.QuestionFindConditionGetReq;
import com.ssafy.solive.api.matching.request.QuestionModifyPutReq;
import com.ssafy.solive.api.matching.request.QuestionRegistPostReq;
import com.ssafy.solive.api.matching.response.QuestionFindConditionRes;
import com.ssafy.solive.api.matching.response.QuestionFindDetailRes;
import com.ssafy.solive.api.matching.service.QuestionService;
import com.ssafy.solive.api.user.service.UserService;
import com.ssafy.solive.common.exception.matching.MatchingPossessionFailException;
import com.ssafy.solive.common.model.CommonResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 학생이 풀이를 원하는 문제를 서버에 등록할 때 필요한 API를 모은 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/question")
@CrossOrigin("*")
public class QuestionController {

    private static final String SUCCESS = "success";  // API 성공 시 return

    private final QuestionService questionService;
    private final UserService userService;

    @Autowired
    public QuestionController(QuestionService questionService, UserService userService) {
        this.questionService = questionService;
        this.userService = userService;
    }

    /**
     * 유저(학생)가 문제를 등록하기 위한 API
     *
     * @param registInfo : 문제 등록할 때 입력한 정보
     * @param fileList   : 문제 사진. 문제는 반드시 사진이 하나 이상 있어야 하므로 Null 불가능
     */
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public CommonResponse<?> regist(@RequestPart QuestionRegistPostReq registInfo,
                                    @RequestPart("files") List<MultipartFile> fileList, HttpServletRequest request) {

        String accessToken = request.getHeader("access-token");
        Long userId = userService.getUserIdByToken(accessToken);
        log.info("QuestionController_regist_start: " + registInfo.toString()
                + ", " + fileList.toString() + ", " + userId);

        registInfo.setStudentId(userId);

        questionService.registQuestion(registInfo, fileList);

        log.info("QuestionController_regist_end: success");
        return CommonResponse.success(SUCCESS);
    }

    /**
     * 유저(학생)이 문제를 삭제하기 위한 API
     *
     * @param deleteInfo : 문제 삭제하기 위해 필요한 정보
     */
    @PutMapping("/delete")
    public CommonResponse<?> delete(@RequestBody QuestionDeletePutReq deleteInfo,
                                    HttpServletRequest request) {

        String accessToken = request.getHeader("access-token");
        Long userId = userService.getUserIdByToken(accessToken);
        log.info("QuestionController_delete_start: " + deleteInfo.toString() + ", " + userId);

        deleteInfo.setStudentId(userId);

        boolean isDeleted = questionService.deleteQuestion(deleteInfo); // 삭제 실패하면 false
        if (isDeleted) {
            log.info("QuestionController_delete_end: success");
            return CommonResponse.success(SUCCESS);
        } else {    // 유저가 작성하지 않은 문제를 삭제하려고 하는 경우
            throw new MatchingPossessionFailException();
        }
    }

    /**
     * 유저(학생)이 문제를 수정하기 위한 API
     *
     * @param modifyInfo : 문제 수정하기 위해 필요한 정보
     */
    @PutMapping()
    public CommonResponse<?> modify(@RequestBody QuestionModifyPutReq modifyInfo,
                                    HttpServletRequest request) {

        String accessToken = request.getHeader("access-token");
        Long userId = userService.getUserIdByToken(accessToken);
        log.info("QuestionController_modify_start: " + modifyInfo.toString() + ", " + userId);

        modifyInfo.setStudentId(userId);

        boolean isModified = questionService.modifyQuestion(modifyInfo); // 수정 실패하면 false
        if (isModified) {
            log.info("QuestionController_modify_end: success");
            return CommonResponse.success(SUCCESS);
        } else {    // 유저가 작성하지 않은 문제를 수정하려고 하는 경우
            throw new MatchingPossessionFailException();
        }
    }

    /**
     * 유저(강사)가 문제를 검색하기 위한 API
     *
     * @param findCondition : 검색 조건. 제목 검색어, 과목 코드, 시간 순 정렬 조건 선택 가능
     */
    @GetMapping()
    public CommonResponse<?> findByCondition(QuestionFindConditionGetReq findCondition) {

        log.info("QuestionController_findByCondition_start: " + findCondition.toString());

        List<QuestionFindConditionRes> findResList = questionService.findByCondition(findCondition);

        if (findResList.size() == 0) {
            log.info("QuestionController_findByCondition_end: No Result");
        } else {
            log.info("QuestionController_findByCondition_end: " + findResList);
        }
        return CommonResponse.success(findResList);
    }

    /**
     * 유저가 문제의 상세 정보를 확인하기 위한 API
     *
     * @param id : question id
     */
    @GetMapping("/{id}")
    public CommonResponse<?> findDetail(@PathVariable Long id) {

        log.info("QuestionController_findDetail_start: " + id);

        QuestionFindDetailRes findDetailRes = questionService.findDetail(id);

        if (findDetailRes == null) {
            log.info("QuestionController_findDetail_end: No Data");
        } else {
            log.info("QuestionController_findDetail_end: " + findDetailRes);
        }
        return CommonResponse.success(findDetailRes);
    }
}
